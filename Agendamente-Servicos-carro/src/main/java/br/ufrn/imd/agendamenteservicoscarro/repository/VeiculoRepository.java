package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    /** Busca um veículo pela placa (única). */
    Optional<Veiculo> findByPlaca(String placa);

    /** Verifica se já existe veículo com a placa informada. */
    boolean existsByPlaca(String placa);

    /** Lista todos os veículos de um cliente. */
    List<Veiculo> findByClienteId(Long clienteId);
}
