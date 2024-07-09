package faeterj.sistema_loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import faeterj.sistema_loja.models.ClientesModel;
import faeterj.sistema_loja.models.ComprasModel;
import faeterj.sistema_loja.models.EstoqueMovimentacaoModel;
import faeterj.sistema_loja.models.ItensPedidoModel;
import faeterj.sistema_loja.models.PedidosModel;
import faeterj.sistema_loja.models.ProdutosModel;
import faeterj.sistema_loja.repository.ClienteRepo;
import faeterj.sistema_loja.repository.ComprasRepo;
import faeterj.sistema_loja.repository.ItensPedidoRepo;
import faeterj.sistema_loja.repository.MoviEstoqueRepo;
import faeterj.sistema_loja.repository.PedidosRepo;
import faeterj.sistema_loja.repository.ProdutosRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ImportacaoService {

    @Autowired
    private ClienteRepo clienteRepository;

    @Autowired
    private ProdutosRepo produtoRepository;

    @Autowired
    private PedidosRepo pedidoRepository;

    @Autowired
    private ItensPedidoRepo itemPedidoRepository;

    @Autowired
    private ComprasRepo comprasRepository;

    @Autowired
    private MoviEstoqueRepo estoqueMovimentacaoRepository;

    public String processarArquivoCSV(MultipartFile arquivoCSV) throws IOException {
        try (InputStream is = arquivoCSV.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String linha;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Ignora a primeira linha
            br.readLine();

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                System.out.println("Dados do CSV: " + Arrays.toString(dados));
   
                if (dados.length >= 20) {
                    try {
                                        
                        if (clienteRepository.findByCpf(dados[6]) == null) {
                            ClientesModel cliente = new ClientesModel();
                            cliente.setEmail(dados[4]);
                            cliente.setBuyer_name(dados[5]);
                            cliente.setCpf(dados[6]);
                            cliente.setBuyer_phone_number(dados[7]);
                            clienteRepository.save(cliente);
                        }
                        
                        ProdutosModel produto = produtoRepository.findBySku(dados[8]);
                        if (produto == null) {
                            produto = new ProdutosModel();
                            produto.setSku(dados[8]);
                            produto.setProduct_name(dados[9]);
                            produto.setItem_price(Double.parseDouble(dados[11]));
                            produto.setQuantity_in_stock(Integer.parseInt(dados[20]));
                            produtoRepository.save(produto);
                        }

                        PedidosModel pedido = new PedidosModel();
                        pedido.setOrder_date(LocalDate.parse(dados[2], formatter));
                        pedido.setPayments_date(LocalDate.parse(dados[3], formatter));
                        pedido.setShip_service_level(dados[12]);
                        pedido.setShip_address_1(dados[13]);
                        pedido.setShip_address_2(dados[14]);
                        pedido.setShip_address_3(dados[15]);
                        pedido.setShip_city(dados[16]);
                        pedido.setShip_state(dados[17]);
                        pedido.setShip_postal_code(dados[18]);
                        pedido.setShip_country(dados[19]);
                        pedidoRepository.save(pedido);

                        ItensPedidoModel itemPedido = new ItensPedidoModel();
                        itemPedido.setPedido(pedido);
                        itemPedido.setProduto(produto);
                        itemPedido.setQuantity_purchased(Integer.parseInt(dados[10]));
                        itemPedido.setItem_price(Double.parseDouble(dados[11]));
                        itemPedidoRepository.save(itemPedido);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Erro ao processar o arquivo CSV: " + e.getMessage();
                    }
                } else {
                    System.err.println("Número de campos no CSV não é suficiente para processamento.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao processar o arquivo CSV: " + e.getMessage();
        }

        return "Importação concluída com sucesso!";
    }

    public void processarPedidos() {
        List<PedidosModel> pedidos = pedidoRepository.findAll();
        pedidos.sort(Comparator.comparingDouble(this::calcularValorTotalPedido).reversed());

        for (PedidosModel pedido : pedidos) {
            boolean pedidoAtendido = true;
            List<ItensPedidoModel> itens = itemPedidoRepository.findByPedido(pedido);
            for (ItensPedidoModel itemPedido : itens) {
                ProdutosModel produto = itemPedido.getProduto();
                if (produto.getQuantity_in_stock() < itemPedido.getQuantity_purchased()) {
                    pedidoAtendido = false;
                    registrarCompra(itemPedido);
                    break;
                }
            }

            if (pedidoAtendido) {
                for (ItensPedidoModel itemPedido : itens) {
                    ProdutosModel produto = itemPedido.getProduto();
                    int quantidadeMovimentada = itemPedido.getQuantity_purchased();
                    int saldoEstoque = produto.getQuantity_in_stock() - quantidadeMovimentada;

                    EstoqueMovimentacaoModel movimentacao = new EstoqueMovimentacaoModel();
                    movimentacao.setProduto(produto);
                    movimentacao.setQuantidadeMovimentada(quantidadeMovimentada);
                    movimentacao.setSaldoEstoque(saldoEstoque);
                    movimentacao.setDataMovimentacao(LocalDate.now());
                    estoqueMovimentacaoRepository.save(movimentacao);

                    produto.setQuantity_in_stock(saldoEstoque);
                    produtoRepository.save(produto);
                }
            }
        }
    }

    private void registrarCompra(ItensPedidoModel itemPedido) {
        ComprasModel compra = new ComprasModel();
        compra.setSku(itemPedido.getProduto().getSku());
        compra.setProduct_name(itemPedido.getProduto().getProduct_name());
        compra.setQuantidadeComprada(itemPedido.getQuantity_purchased());
        compra.setStatus("Pendente");
        comprasRepository.save(compra);
    }

    private double calcularValorTotalPedido(PedidosModel pedido) {
        return itemPedidoRepository.findByPedido(pedido).stream()
                .mapToDouble(item -> item.getQuantity_purchased() * item.getItem_price())
                .sum();
    }

    public void processarCompras() {
        List<ComprasModel> compras = comprasRepository.findAll();

        for (ComprasModel compra : compras) {
            ProdutosModel produto = produtoRepository.findBySku(compra.getSku());

            if (produto != null) {
                produto.setQuantity_in_stock(produto.getQuantity_in_stock() + compra.getQuantidadeComprada());
                produtoRepository.save(produto);
            }
            compra.setStatus("FINALIZADO");
            comprasRepository.save(compra);
        }
    }

    public List<ClientesModel> listarClientes() {
        return clienteRepository.findAll();
    }
    public List<ProdutosModel> listarProdutos() {
        return produtoRepository.findAll();
    }

    public List<PedidosModel> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<ItensPedidoModel> listarItensPedido() {
        return itemPedidoRepository.findAll();
    }

    public void deletarTodosOsDados() {
        estoqueMovimentacaoRepository.deleteAll();
        comprasRepository.deleteAll(); 
        itemPedidoRepository.deleteAll();
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();     
              
    }
}
