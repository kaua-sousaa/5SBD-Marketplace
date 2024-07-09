package faeterj.sistema_loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import faeterj.sistema_loja.models.PedidosModel;

public interface PedidosRepo extends JpaRepository<PedidosModel, Integer>{
    
}
