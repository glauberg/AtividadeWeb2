package br.ufrn.imd.agendamenteservicoscarro.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Documento de auditoria persistido no MongoDB Atlas (database: auditoria).
 * Cada registro representa uma operação executada sobre uma entidade de negócio.
 */
@Document(collection = "log_auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogAuditoria {

    @Id
    private String id;

    /** Tipo da operação: CRIAR, ATUALIZAR, EXCLUIR, CONSULTAR, LOGIN, LOGOUT */
    @Indexed
    private String operacao;

    /** Nome da entidade afetada: Agendamento, Cliente, Veiculo, Usuario... */
    @Indexed
    private String entidade;

    /** ID da entidade afetada no banco relacional. */
    private Long entidadeId;

    /** E-mail (ou identificador) do usuário que executou a ação. */
    @Indexed
    private String usuarioEmail;

    /** Instante exato em que a operação foi realizada (UTC). */
    @Indexed
    private Instant timestamp;

    /** Detalhe adicional ou mensagem descritiva sobre a operação. */
    private String detalhe;
}
