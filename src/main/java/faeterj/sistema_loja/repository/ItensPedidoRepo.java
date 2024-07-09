package faeterj.sistema_loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import faeterj.sistema_loja.models.ItensPedidoModel;
import faeterj.sistema_loja.models.PedidosModel;

public interface ItensPedidoRepo extends JpaRepository<ItensPedidoModel, Integer>{
    List<ItensPedidoModel> findByPedido(PedidosModel pedido);
}
