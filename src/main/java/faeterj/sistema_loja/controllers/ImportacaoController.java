package faeterj.sistema_loja.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import faeterj.sistema_loja.models.ClientesModel;
import faeterj.sistema_loja.models.ItensPedidoModel;
import faeterj.sistema_loja.models.PedidosModel;
import faeterj.sistema_loja.models.ProdutosModel;
import faeterj.sistema_loja.service.ImportacaoService;
import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/importacao")
public class ImportacaoController {

    @Autowired
    private ImportacaoService importacaoService;

    @PostMapping(value = "/importacao/csv", consumes = {"multipart/form-data"})
    @Operation(summary = "Importar dados de arquivo CSV")
    public String importarDadosCSV(
        @RequestParam("cargaFile") @RequestBody MultipartFile arquivoCSV
    ) throws IOException {
        // Implementação para processar o arquivo CSV
        return importacaoService.processarArquivoCSV(arquivoCSV);
    }

    
    // Exemplos adicionais de operações para listar entidades, se necessário
  /* 
    @GetMapping("/clientes")
    public List<ClientesModel> listarClientes() {
        return importacaoService.listarClientes();
    }

    @GetMapping("/produtos")
    public List<ProdutosModel> listarProdutos() {
        return importacaoService.listarProdutos();
    }

    @GetMapping("/pedidos")
    public List<PedidosModel> listarPedidos() {
        return importacaoService.listarPedidos();
    }

    @GetMapping("/itens-pedido")
    public List<ItensPedidoModel> listarItensPedido() {
        return importacaoService.listarItensPedido();
    }
        */

    @DeleteMapping("/deletarRegistros")
    public ResponseEntity<String> deletarRegistros() {
        importacaoService.deletarTodosOsDados();
        return ResponseEntity.ok("Registros deletados com sucesso!");
    }

    @GetMapping("/processar-pedidos")
    public ResponseEntity<String> processarPedidos() {
        importacaoService.processarPedidos();
        return ResponseEntity.ok("Pedidos processados com sucesso!");
    }

    @GetMapping("/processar-compras")
    public ResponseEntity<String> processarCompras() {
        importacaoService.processarCompras();
        return ResponseEntity.ok("Compras processadas com sucesso!");
    }
}
