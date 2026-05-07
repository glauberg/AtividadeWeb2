package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.audit.AuditoriaService;
import br.ufrn.imd.agendamenteservicoscarro.audit.LogAuditoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para consulta dos logs de auditoria armazenados no MongoDB
 * Atlas.
 * Todos os endpoints são protegidos (requerem autenticação com perfil GERENTE).
 */
@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
@Tag(name = "Auditoria", description = "Consulta de logs de auditoria (MongoDB)")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    @Operation(summary = "Lista todos os logs de auditoria")
    public ResponseEntity<List<LogAuditoria>> listarTodos() {
        return ResponseEntity.ok(auditoriaService.listarTodos());
    }

    @GetMapping("/entidade/{entidade}")
    @Operation(summary = "Lista logs de uma entidade específica (ex: Agendamento, Cliente)")
    public ResponseEntity<List<LogAuditoria>> porEntidade(@PathVariable String entidade) {
        return ResponseEntity.ok(auditoriaService.buscarPorEntidade(entidade));
    }

    @GetMapping("/entidade/{entidade}/{id}")
    @Operation(summary = "Lista logs de uma instância específica de uma entidade")
    public ResponseEntity<List<LogAuditoria>> porEntidadeEId(
            @PathVariable String entidade,
            @PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarPorEntidadeEId(entidade, id));
    }

    @GetMapping("/usuario/{email}")
    @Operation(summary = "Lista logs de um usuário pelo e-mail")
    public ResponseEntity<List<LogAuditoria>> porUsuario(@PathVariable String email) {
        return ResponseEntity.ok(auditoriaService.buscarPorUsuario(email));
    }

    @GetMapping("/operacao/{operacao}")
    @Operation(summary = "Lista logs por tipo de operação (CRIAR, ATUALIZAR_STATUS, EXCLUIR...)")
    public ResponseEntity<List<LogAuditoria>> porOperacao(@PathVariable String operacao) {
        return ResponseEntity.ok(auditoriaService.buscarPorOperacao(operacao));
    }
}
