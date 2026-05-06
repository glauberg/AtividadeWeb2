package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * GET /clientes
     * Retorna todos os clientes. Aceita filtro opcional por nome:
     *   GET /clientes?nome=Jo
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listar(
            @RequestParam(required = false) String nome) {
        List<Cliente> resultado = (nome != null && !nome.isBlank())
                ? clienteService.buscarPorNome(nome)
                : clienteService.listarTodos();
        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /clientes/{id}
     * Retorna um cliente pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clienteService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /clientes
     * Cadastra um novo cliente.
     */
    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestBody Cliente cliente) {
        try {
            Cliente salvo = clienteService.cadastrar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * PUT /clientes/{id}
     * Atualiza os dados de um cliente existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable Long id,
            @RequestBody Cliente dadosNovos) {
        try {
            return ResponseEntity.ok(clienteService.atualizar(id, dadosNovos));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * DELETE /clientes/{id}
     * Remove um cliente pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remover(@PathVariable Long id) {
        try {
            clienteService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
