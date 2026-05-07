package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Agendamento;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    /**
     * Busca todos os agendamentos fazendo JOIN FETCH dos serviços em uma única query.
     * DISTINCT evita duplicatas causadas pelo JOIN com a tabela de associação.
     */
    @Query("SELECT DISTINCT a FROM Agendamento a LEFT JOIN FETCH a.servicos")
    List<Agendamento> findAllComServicos();

    /**
     * Busca agendamentos de um cliente com serviços já carregados (sem duplicatas).
     */
    @Query("SELECT DISTINCT a FROM Agendamento a LEFT JOIN FETCH a.servicos WHERE a.cliente.id = :clienteId")
    List<Agendamento> findByClienteIdComServicos(@Param("clienteId") Long clienteId);

    /** Lista agendamentos por status. */
    List<Agendamento> findByStatus(StatusAgendamento status);

    /** Lista agendamentos de um mecânico específico. */
    List<Agendamento> findByMecanicoResponsavelId(Long funcionarioId);
}
