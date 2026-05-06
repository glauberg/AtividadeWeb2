package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.model.Veiculo;
import br.ufrn.imd.agendamenteservicoscarro.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    /**
     * GET /veiculos
     * Lista todos os veículos. Aceita filtro por cliente:
     *   GET /veiculos?clienteId=2
     */
    @GetMapping
    public ResponseEntity<List<Veiculo>> listar(
            @RequestParam(required = false) Long clienteId) {
        List<Veiculo> resultado = (clienteId != null)
                ? veiculoService.listarPorCliente(clienteId)
                : veiculoService.listarTodos();
        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /veiculos/{id}
     * Retorna um veículo pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(veiculoService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /veiculos?clienteId={clienteId}
     * Cadastra um novo veículo vinculado ao cliente informado.
     */
    @PostMapping
    public ResponseEntity<Object> cadastrar(
            @RequestParam Long clienteId,
            @RequestBody Veiculo veiculo) {
        try {
            Veiculo salvo = veiculoService.cadastrar(clienteId, veiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * PUT /veiculos/{id}
     * Atualiza placa, modelo e marca de um veículo.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable Long id,
            @RequestBody Veiculo dadosNovos) {
        try {
            return ResponseEntity.ok(veiculoService.atualizar(id, dadosNovos));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * DELETE /veiculos/{id}
     * Remove um veículo pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remover(@PathVariable Long id) {
        try {
            veiculoService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
