package faeterj.sistema_loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import faeterj.sistema_loja.models.EstoqueMovimentacaoModel;

public interface MoviEstoqueRepo extends JpaRepository<EstoqueMovimentacaoModel, Integer>{
	
}
