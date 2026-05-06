package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    /** Verifica se já existe serviço com o nome informado. */
    boolean existsByNomeIgnoreCase(String nome);

    /** Busca serviços cujo nome contenha o trecho informado (case-insensitive). */
    List<Servico> findByNomeContainingIgnoreCase(String nome);
}
