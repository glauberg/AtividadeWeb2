package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /** Busca um cliente pelo CPF (único). */
    Optional<Cliente> findByCpf(String cpf);

    /** Verifica se já existe cliente com o CPF informado. */
    boolean existsByCpf(String cpf);

    /** Busca clientes cujo nome contenha o trecho informado (case-insensitive). */
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
