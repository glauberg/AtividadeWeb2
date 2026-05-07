package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Produto;
import br.ufrn.imd.agendamenteservicoscarro.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: id=" + id));
    }

    @Transactional
    public Produto cadastrar(Produto produto) {
        if (produto.getEstoque() == null) {
            produto.setEstoque(0);
        }
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, Produto dadosNovos) {
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
        if (!produtoRepository.existsById(id)) {
            throw new NoSuchElementException("Produto não encontrado: id=" + id);
        }
        produtoRepository.deleteById(id);
    }
}
