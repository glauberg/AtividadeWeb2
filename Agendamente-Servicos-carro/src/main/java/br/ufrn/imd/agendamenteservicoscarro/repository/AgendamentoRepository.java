package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Agendamento;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    /** Lista todos os agendamentos de um cliente específico. */
    List<Agendamento> findByClienteId(Long clienteId);

    /** Lista agendamentos por status. */
    List<Agendamento> findByStatus(StatusAgendamento status);

    /** Lista agendamentos de um mecânico específico. */
    List<Agendamento> findByMecanicoResponsavelId(Long funcionarioId);
}
