package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.model.Funcionario;
import br.ufrn.imd.agendamenteservicoscarro.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    /**
     * GET /funcionarios
     * Retorna todos os funcionários. Aceita filtro opcional por nome:
     *   GET /funcionarios?nome=Pedro
     */
    @GetMapping
    public ResponseEntity<List<Funcionario>> listar(
            @RequestParam(required = false) String nome) {
        List<Funcionario> resultado = (nome != null && !nome.isBlank())
                ? funcionarioService.buscarPorNome(nome)
                : funcionarioService.listarTodos();
        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /funcionarios/{id}
     * Retorna um funcionário pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(funcionarioService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /funcionarios
     * Cadastra um novo funcionário.
     */
    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestBody Funcionario funcionario) {
        try {
            Funcionario salvo = funcionarioService.cadastrar(funcionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * PUT /funcionarios/{id}
     * Atualiza os dados de um funcionário existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable Long id,
            @RequestBody Funcionario dadosNovos) {
        try {
            return ResponseEntity.ok(funcionarioService.atualizar(id, dadosNovos));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * DELETE /funcionarios/{id}
     * Remove um funcionário pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remover(@PathVariable Long id) {
        try {
            funcionarioService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
