package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.model.Servico;
import br.ufrn.imd.agendamenteservicoscarro.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    /**
     * GET /servicos
     * Lista todos os serviços do catálogo. Aceita filtro por nome:
     *   GET /servicos?nome=Óleo
     */
    @GetMapping
    public ResponseEntity<List<Servico>> listar(
            @RequestParam(required = false) String nome) {
        List<Servico> resultado = (nome != null && !nome.isBlank())
                ? servicoService.buscarPorNome(nome)
                : servicoService.listarTodos();
        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /servicos/{id}
     * Retorna um serviço pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(servicoService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /servicos
     * Cadastra um novo serviço no catálogo.
     */
    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestBody Servico servico) {
        try {
            Servico salvo = servicoService.cadastrar(servico);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * PUT /servicos/{id}
     * Atualiza nome e preço base de um serviço.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable Long id,
            @RequestBody Servico dadosNovos) {
        try {
            return ResponseEntity.ok(servicoService.atualizar(id, dadosNovos));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * DELETE /servicos/{id}
     * Remove um serviço do catálogo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remover(@PathVariable Long id) {
        try {
            servicoService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
