package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Funcionario;
import br.ufrn.imd.agendamenteservicoscarro.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    /** Lista todos os funcionários cadastrados. */
    @Transactional(readOnly = true)
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    /** Busca um funcionário pelo ID. Lança exceção se não encontrado. */
    @Transactional(readOnly = true)
    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado: id=" + id));
    }

    /** Busca funcionários cujo nome contenha o trecho informado (case-insensitive). */
    @Transactional(readOnly = true)
    public List<Funcionario> buscarPorNome(String nome) {
        return funcionarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    /** Cadastra um novo funcionário. Valida duplicidade de CPF. */
    @Transactional
    public Funcionario cadastrar(Funcionario funcionario) {
        if (funcionarioRepository.existsByCpf(funcionario.getCpf())) {
            throw new IllegalArgumentException("Já existe um funcionário com o CPF informado.");
        }
        return funcionarioRepository.save(funcionario);
    }

    /** Atualiza os dados de um funcionário existente. */
    @Transactional
    public Funcionario atualizar(Long id, Funcionario dadosNovos) {
        Funcionario existente = buscarPorId(id);

        // Valida CPF duplicado apenas se for diferente do atual
        if (!existente.getCpf().equals(dadosNovos.getCpf())
                && funcionarioRepository.existsByCpf(dadosNovos.getCpf())) {
            throw new IllegalArgumentException("Já existe um funcionário com o CPF informado.");
        }

        existente.setNome(dadosNovos.getNome());
        existente.setCpf(dadosNovos.getCpf());
        existente.setTelefone(dadosNovos.getTelefone());

        return funcionarioRepository.save(existente);
    }

    /** Remove um funcionário pelo ID. */
    @Transactional
    public void remover(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new NoSuchElementException("Funcionário não encontrado: id=" + id);
        }
        funcionarioRepository.deleteById(id);
    }
}
