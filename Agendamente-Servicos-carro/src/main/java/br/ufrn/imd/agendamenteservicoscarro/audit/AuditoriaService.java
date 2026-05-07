package br.ufrn.imd.agendamenteservicoscarro.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Serviço responsável por registrar eventos de auditoria no MongoDB Atlas.
 * Deve ser injetado em qualquer Service de negócio que precise gerar logs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    /**
     * Registra um evento de auditoria.
     *
     * @param operacao     tipo da operação (ex: "CRIAR", "ATUALIZAR", "EXCLUIR")
     * @param entidade     nome da entidade afetada (ex: "Agendamento", "Cliente")
     * @param entidadeId   ID relacional da entidade (pode ser null para operações
     *                     gerais)
     * @param usuarioEmail e-mail do usuário executor (pode ser "SISTEMA" para
     *                     tarefas automáticas)
     * @param detalhe      mensagem descritiva adicional
     */
    public void registrar(String operacao,
            String entidade,
            Long entidadeId,
            String usuarioEmail,
            String detalhe) {
        LogAuditoria logEntry = LogAuditoria.builder()
                .operacao(operacao)
                .entidade(entidade)
                .entidadeId(entidadeId)
                .usuarioEmail(usuarioEmail != null ? usuarioEmail : "ANONIMO")
                .timestamp(Instant.now())
                .detalhe(detalhe)
                .build();
        try {
            logAuditoriaRepository.save(logEntry);
        } catch (Exception ex) {
            // Auditoria não deve quebrar o fluxo principal de negócio
            log.error("[AUDITORIA] Falha ao salvar log no MongoDB: {}", ex.getMessage(), ex);
        }
    }

    /** Atalho para operação sem detalhe adicional. */
    public void registrar(String operacao, String entidade, Long entidadeId, String usuarioEmail) {
        registrar(operacao, entidade, entidadeId, usuarioEmail, null);
    }

    // ----------------------------------------------------------------
    // Consultas
    // ----------------------------------------------------------------

    /** Retorna todos os logs de uma entidade específica. */
    public List<LogAuditoria> buscarPorEntidade(String entidade) {
        return logAuditoriaRepository.findByEntidade(entidade);
    }

    /** Retorna logs de uma instância específica de uma entidade. */
    public List<LogAuditoria> buscarPorEntidadeEId(String entidade, Long entidadeId) {
        return logAuditoriaRepository.findByEntidadeAndEntidadeId(entidade, entidadeId);
    }

    /** Retorna todos os logs de um usuário. */
    public List<LogAuditoria> buscarPorUsuario(String email) {
        return logAuditoriaRepository.findByUsuarioEmail(email);
    }

    /** Retorna logs por tipo de operação. */
    public List<LogAuditoria> buscarPorOperacao(String operacao) {
        return logAuditoriaRepository.findByOperacao(operacao);
    }

    /** Retorna todos os logs. */
    public List<LogAuditoria> listarTodos() {
        return logAuditoriaRepository.findAll();
    }
}
