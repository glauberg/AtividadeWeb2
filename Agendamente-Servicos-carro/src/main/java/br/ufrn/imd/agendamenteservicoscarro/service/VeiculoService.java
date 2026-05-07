package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.model.Veiculo;
import br.ufrn.imd.agendamenteservicoscarro.repository.ClienteRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    /** Lista todos os veículos cadastrados. */
    @Transactional(readOnly = true)
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    /** Lista todos os veículos de um cliente específico. */
    @Transactional(readOnly = true)
    public List<Veiculo> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new NoSuchElementException("Cliente não encontrado: id=" + clienteId);
        }
        return veiculoRepository.findByClienteId(clienteId);
    }

    /** Busca um veículo pelo ID. */
    @Transactional(readOnly = true)
    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Veículo não encontrado: id=" + id));
    }

    /** Cadastra um novo veículo vinculado a um cliente. Valida placa duplicada. */
    @Transactional
    public Veiculo cadastrar(Long clienteId, Veiculo veiculo) {
        if (veiculoRepository.existsByPlaca(veiculo.getPlaca())) {
            throw new IllegalArgumentException("Já existe um veículo com a placa informada.");
        }
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado: id=" + clienteId));
        veiculo.setCliente(cliente);
        return veiculoRepository.save(veiculo);
    }

    /** Atualiza os dados de um veículo. Valida placa duplicada se alterada. */
    @Transactional
    public Veiculo atualizar(Long id, Veiculo dadosNovos) {
        Veiculo existente = buscarPorId(id);

        if (!existente.getPlaca().equals(dadosNovos.getPlaca())
                && veiculoRepository.existsByPlaca(dadosNovos.getPlaca())) {
            throw new IllegalArgumentException("Já existe um veículo com a placa informada.");
        }

        existente.setPlaca(dadosNovos.getPlaca());
        existente.setModelo(dadosNovos.getModelo());
        existente.setMarca(dadosNovos.getMarca());

        return veiculoRepository.save(existente);
    }

    /** Remove um veículo pelo ID. */
    @Transactional
    public void remover(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new NoSuchElementException("Veículo não encontrado: id=" + id);
        }
        veiculoRepository.deleteById(id);
    }
}
