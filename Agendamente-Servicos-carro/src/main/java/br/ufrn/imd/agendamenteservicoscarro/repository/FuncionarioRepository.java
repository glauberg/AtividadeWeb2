package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    /** Busca um funcionário pelo CPF (único). */
    Optional<Funcionario> findByCpf(String cpf);

    /** Verifica se já existe funcionário com o CPF informado. */
    boolean existsByCpf(String cpf);

    /** Busca funcionários cujo nome contenha o trecho informado (case-insensitive). */
    List<Funcionario> findByNomeContainingIgnoreCase(String nome);
}
