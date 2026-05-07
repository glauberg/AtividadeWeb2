package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.model.Veiculo;
import br.ufrn.imd.agendamenteservicoscarro.repository.ClienteRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioLogadoService usuarioLogadoService;

    @Transactional(readOnly = true)
    public List<Veiculo> listarTodos() {
        if (usuarioLogadoService.ehCliente()) {
            return veiculoRepository.findByClienteId(usuarioLogadoService.clienteIdObrigatorio());
        }
        return veiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Veiculo> listarPorCliente(Long clienteId) {
        if (usuarioLogadoService.ehCliente()) {
            Long clienteLogadoId = usuarioLogadoService.clienteIdObrigatorio();
            if (!clienteLogadoId.equals(clienteId)) {
                throw new AccessDeniedException("Cliente so pode ver seus proprios veiculos.");
            }
        }

        if (!clienteRepository.existsById(clienteId)) {
            throw new NoSuchElementException("Cliente nao encontrado: id=" + clienteId);
        }
        return veiculoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public Veiculo buscarPorId(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Veiculo nao encontrado: id=" + id));
        validarAcessoCliente(veiculo);
        return veiculo;
    }

    @Transactional
    public Veiculo cadastrar(Long clienteId, Veiculo veiculo) {
        Long clienteAlvoId = usuarioLogadoService.ehCliente()
                ? usuarioLogadoService.clienteIdObrigatorio()
                : clienteId;

        if (veiculoRepository.existsByPlaca(veiculo.getPlaca())) {
            throw new IllegalArgumentException("Ja existe um veiculo com a placa informada.");
        }

        Cliente cliente = clienteRepository.findById(clienteAlvoId)
                .orElseThrow(() -> new NoSuchElementException("Cliente nao encontrado: id=" + clienteAlvoId));
        veiculo.setCliente(cliente);
        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public Veiculo atualizar(Long id, Veiculo dadosNovos) {
        Veiculo existente = buscarPorId(id);

        if (!existente.getPlaca().equals(dadosNovos.getPlaca())
                && veiculoRepository.existsByPlaca(dadosNovos.getPlaca())) {
            throw new IllegalArgumentException("Ja existe um veiculo com a placa informada.");
        }

        existente.setPlaca(dadosNovos.getPlaca());
        existente.setModelo(dadosNovos.getModelo());
        existente.setMarca(dadosNovos.getMarca());

        return veiculoRepository.save(existente);
    }

    @Transactional
    public void remover(Long id) {
        Veiculo veiculo = buscarPorId(id);
        veiculoRepository.delete(veiculo);
    }

    private void validarAcessoCliente(Veiculo veiculo) {
        if (usuarioLogadoService.ehCliente()
                && !veiculo.getCliente().getId().equals(usuarioLogadoService.clienteIdObrigatorio())) {
            throw new AccessDeniedException("Cliente so pode acessar seus proprios veiculos.");
        }
    }
}
