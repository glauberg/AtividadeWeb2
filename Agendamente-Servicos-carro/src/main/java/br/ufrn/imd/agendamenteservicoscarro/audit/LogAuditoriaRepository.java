package br.ufrn.imd.agendamenteservicoscarro.audit;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repositório Spring Data MongoDB para a coleção log_auditoria.
 * Isolado dos repositórios JPA — usa MongoRepository, não JpaRepository.
 */
@Repository
public interface LogAuditoriaRepository extends MongoRepository<LogAuditoria, String> {

    /** Busca todos os logs de uma determinada entidade. */
    List<LogAuditoria> findByEntidade(String entidade);

    /** Busca todos os logs de uma entidade específica pelo ID relacional. */
    List<LogAuditoria> findByEntidadeAndEntidadeId(String entidade, Long entidadeId);

    /** Busca todos os logs realizados por um usuário. */
    List<LogAuditoria> findByUsuarioEmail(String usuarioEmail);

    /** Busca logs em um intervalo de tempo. */
    List<LogAuditoria> findByTimestampBetween(Instant inicio, Instant fim);

    /** Busca logs por tipo de operação. */
    List<LogAuditoria> findByOperacao(String operacao);
}
