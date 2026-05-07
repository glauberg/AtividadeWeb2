package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    /** Busca uma pessoa pelo CPF (único). */
    Optional<Pessoa> findByCpf(String cpf);

    /** Verifica se já existe cadastro com o CPF informado. */
    boolean existsByCpf(String cpf);
}
