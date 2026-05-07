package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /** Lista todos os clientes cadastrados. */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    /** Busca um cliente pelo ID. Lança exceção se não encontrado. */
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado: id=" + id));
    }

    /** Busca clientes cujo nome contenha o trecho informado (case-insensitive). */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    /** Cadastra um novo cliente. Valida duplicidade de CPF. */
    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("Já existe um cliente com o CPF informado.");
        }
        return clienteRepository.save(cliente);
    }

    /** Atualiza os dados de um cliente existente. */
    @Transactional
    public Cliente atualizar(Long id, Cliente dadosNovos) {
        Cliente existente = buscarPorId(id);

        // Valida CPF duplicado apenas se for diferente do atual
        if (!existente.getCpf().equals(dadosNovos.getCpf())
                && clienteRepository.existsByCpf(dadosNovos.getCpf())) {
            throw new IllegalArgumentException("Já existe um cliente com o CPF informado.");
        }

        existente.setNome(dadosNovos.getNome());
        existente.setCpf(dadosNovos.getCpf());
        existente.setTelefone(dadosNovos.getTelefone());

        return clienteRepository.save(existente);
    }

    /** Remove um cliente pelo ID. */
    @Transactional
    public void remover(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new NoSuchElementException("Cliente não encontrado: id=" + id);
        }
        clienteRepository.deleteById(id);
    }
}
