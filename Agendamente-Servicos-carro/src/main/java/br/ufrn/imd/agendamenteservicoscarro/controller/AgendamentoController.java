package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.dto.AgendamentoRequest;
import br.ufrn.imd.agendamenteservicoscarro.dto.AgendamentoResponse;
import br.ufrn.imd.agendamenteservicoscarro.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    /**
     * GET /agendamentos
     * Lista todos os agendamentos. Aceita filtro por cliente:
     *   GET /agendamentos?clienteId=2  (usado em MeusAgendamentosPage)
     */
    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listar(
            @RequestParam(required = false) Long clienteId) {
        return ResponseEntity.ok(agendamentoService.listar(clienteId));
    }

    /**
     * GET /agendamentos/{id}
     * Retorna um agendamento pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(agendamentoService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /agendamentos
     * Cria um novo agendamento.
     * Body: { veiculoId, dataHora, mecanicoId, servicoIds }
     */
    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody AgendamentoRequest request) {
        try {
            AgendamentoResponse criado = agendamentoService.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * PATCH /agendamentos/{id}/status
     * Atualiza o status de um agendamento.
     * Body: { status: "EM_MANUTENCAO" | "CONCLUIDO" | "CANCELADO" }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Object> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String novoStatus = body.get("status");
            return ResponseEntity.ok(agendamentoService.atualizarStatus(id, novoStatus));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
