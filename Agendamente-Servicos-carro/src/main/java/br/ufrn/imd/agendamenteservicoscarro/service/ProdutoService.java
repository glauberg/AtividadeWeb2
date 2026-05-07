package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Produto;
import br.ufrn.imd.agendamenteservicoscarro.model.Servico;
import br.ufrn.imd.agendamenteservicoscarro.repository.ProdutoRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ServicoRepository servicoRepository;
    private final UsuarioLogadoService usuarioLogadoService;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Produto> listarDisponiveisParaServicos(List<Long> servicoIds) {
        List<Produto> disponiveis = produtoRepository.findByEstoqueGreaterThan(0);
        if (servicoIds == null || servicoIds.isEmpty()) {
            return disponiveis;
        }

        List<Servico> servicos = servicoRepository.findAllById(servicoIds);
        Set<String> palavras = palavrasChave(servicos);
        if (palavras.isEmpty()) {
            return disponiveis;
        }

        List<Produto> filtrados = disponiveis.stream()
                .filter(produto -> palavras.stream().anyMatch(palavra -> normalizar(produto.getNome()).contains(palavra)))
                .toList();

        return filtrados.isEmpty() ? disponiveis : filtrados;
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto nao encontrado: id=" + id));
    }

    @Transactional
    public Produto cadastrar(Produto produto) {
        usuarioLogadoService.exigirGerente("Apenas gerente pode cadastrar produto.");
        validarProduto(produto);
        if (produto.getEstoque() == null) {
            produto.setEstoque(0);
        }
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, Produto dadosNovos) {
        usuarioLogadoService.exigirGerente("Apenas gerente pode atualizar produto.");
        validarProduto(dadosNovos);

        Produto existente = buscarPorId(id);
        existente.setNome(dadosNovos.getNome());
        existente.setPrecoVenda(dadosNovos.getPrecoVenda());
        if (dadosNovos.getEstoque() != null) {
            existente.setEstoque(dadosNovos.getEstoque());
        }
        return produtoRepository.save(existente);
    }

    @Transactional
    public void remover(Long id) {
        usuarioLogadoService.exigirGerente("Apenas gerente pode remover produto.");
        if (!produtoRepository.existsById(id)) {
            throw new NoSuchElementException("Produto nao encontrado: id=" + id);
        }
        produtoRepository.deleteById(id);
    }

    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto e obrigatorio.");
        }
        if (produto.getPrecoVenda() == null || produto.getPrecoVenda() < 0) {
            throw new IllegalArgumentException("Preco do produto deve ser maior ou igual a zero.");
        }
        if (produto.getEstoque() != null && produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Estoque do produto nao pode ser negativo.");
        }
    }

    private Set<String> palavrasChave(List<Servico> servicos) {
        String nomes = normalizar(String.join(" ", servicos.stream().map(Servico::getNome).toList()));

        if (nomes.contains("oleo")) {
            return Set.of("oleo", "filtro");
        }
        if (nomes.contains("pastilha") || nomes.contains("freio")) {
            return Set.of("pastilha", "freio");
        }
        if (nomes.contains("revisao")) {
            return Set.of("filtro", "oleo", "vela", "fluido");
        }
        if (nomes.contains("bico")) {
            return Set.of("aditivo", "limpeza", "filtro");
        }
        return Set.of();
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto == null ? "" : texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}
