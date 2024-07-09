package faeterj.sistema_loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import faeterj.sistema_loja.models.ComprasModel;

@Repository
public interface ComprasRepo extends JpaRepository<ComprasModel, Integer> {
}
