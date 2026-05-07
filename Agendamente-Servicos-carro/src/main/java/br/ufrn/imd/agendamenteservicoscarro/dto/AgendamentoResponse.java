package br.ufrn.imd.agendamenteservicoscarro.dto;

import br.ufrn.imd.agendamenteservicoscarro.model.Agendamento;

import java.util.List;

/**
 * Resposta de GET /agendamentos.
 * Formata os dados conforme o frontend espera:
 *   { id, dataHora, status, valorTotal,
 *     cliente: { nome },
 *     veiculo: { modelo, placa },
 *     funcionario: { nome },
 *     servicos: [String]  ← nomes dos serviços
 *   }
 */
public record AgendamentoResponse(
        Long id,
        String dataHora,
        String status,
        Double valorTotal,
        ClienteInfo cliente,
        VeiculoInfo veiculo,
        FuncionarioInfo funcionario,
        List<String> servicos
) {
    public record ClienteInfo(String nome, String telefone) {}
    public record VeiculoInfo(String modelo, String placa) {}
    public record FuncionarioInfo(String nome) {}

    /** Converte uma entidade Agendamento para este DTO. */
    public static AgendamentoResponse from(Agendamento a) {
        return new AgendamentoResponse(
                a.getId(),
                a.getDataHora().toString(),
                a.getStatus().name(),
                a.getValorTotal(),
                new ClienteInfo(a.getCliente().getNome(), a.getCliente().getTelefone()),
                new VeiculoInfo(a.getVeiculo().getModelo(), a.getVeiculo().getPlaca()),
                a.getMecanicoResponsavel() != null
                        ? new FuncionarioInfo(a.getMecanicoResponsavel().getNome())
                        : null,
                a.getServicos().stream().map(s -> s.getNome()).toList()
        );
    }
}
