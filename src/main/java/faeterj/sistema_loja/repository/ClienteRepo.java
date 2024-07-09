package faeterj.sistema_loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import faeterj.sistema_loja.models.ClientesModel;

@Repository
public interface ClienteRepo extends JpaRepository<ClientesModel, Integer>{
    ClientesModel findByCpf(String cpf);
}
