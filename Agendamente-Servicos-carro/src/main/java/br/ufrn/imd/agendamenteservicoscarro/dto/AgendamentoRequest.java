package br.ufrn.imd.agendamenteservicoscarro.dto;

import java.util.List;

/**
 * Corpo da requisição POST /agendamentos.
 * Campos devem corresponder ao formulário do frontend (AgendarServicoPage).
 */
public record AgendamentoRequest(
        Long veiculoId,
        String dataHora,       // formato datetime-local: "2026-05-10T08:00"
        Long mecanicoId,
        List<Long> servicoIds
) {}
