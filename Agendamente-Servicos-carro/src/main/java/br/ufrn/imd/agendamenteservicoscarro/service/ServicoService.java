package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Servico;
import br.ufrn.imd.agendamenteservicoscarro.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    /** Lista todos os serviços do catálogo. */
    @Transactional(readOnly = true)
    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    /** Busca serviços cujo nome contenha o trecho informado (case-insensitive). */
    @Transactional(readOnly = true)
    public List<Servico> buscarPorNome(String nome) {
        return servicoRepository.findByNomeContainingIgnoreCase(nome);
    }

    /** Busca um serviço pelo ID. */
    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Serviço não encontrado: id=" + id));
    }

    /** Cadastra um novo serviço no catálogo. Valida nome duplicado. */
    @Transactional
    public Servico cadastrar(Servico servico) {
        if (servicoRepository.existsByNomeIgnoreCase(servico.getNome())) {
            throw new IllegalArgumentException("Já existe um serviço com esse nome.");
        }
        return servicoRepository.save(servico);
    }

    /** Atualiza nome e preço de um serviço existente. */
    @Transactional
    public Servico atualizar(Long id, Servico dadosNovos) {
        Servico existente = buscarPorId(id);

        if (!existente.getNome().equalsIgnoreCase(dadosNovos.getNome())
                && servicoRepository.existsByNomeIgnoreCase(dadosNovos.getNome())) {
            throw new IllegalArgumentException("Já existe um serviço com esse nome.");
        }

        existente.setNome(dadosNovos.getNome());
        existente.setPrecoBase(dadosNovos.getPrecoBase());

        return servicoRepository.save(existente);
    }

    /** Remove um serviço pelo ID. */
    @Transactional
    public void remover(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new NoSuchElementException("Serviço não encontrado: id=" + id);
        }
        servicoRepository.deleteById(id);
    }
}
