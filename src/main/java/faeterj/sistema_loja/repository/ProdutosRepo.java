package faeterj.sistema_loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import faeterj.sistema_loja.models.ProdutosModel;

@Repository
public interface ProdutosRepo extends JpaRepository<ProdutosModel, String>{
    ProdutosModel findBySku(String sku);
}

