package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.audit.AuditoriaService;
import br.ufrn.imd.agendamenteservicoscarro.dto.AgendamentoRequest;
import br.ufrn.imd.agendamenteservicoscarro.dto.AgendamentoResponse;
import br.ufrn.imd.agendamenteservicoscarro.model.Agendamento;
import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.model.Funcionario;
import br.ufrn.imd.agendamenteservicoscarro.model.Servico;
import br.ufrn.imd.agendamenteservicoscarro.model.Veiculo;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.StatusAgendamento;
import br.ufrn.imd.agendamenteservicoscarro.repository.AgendamentoRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.FuncionarioRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.ServicoRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final VeiculoRepository veiculoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ServicoRepository servicoRepository;
    private final AuditoriaService auditoriaService;

    /** Lista todos os agendamentos, ou filtra por clienteId se fornecido. */
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listar(Long clienteId) {
        List<Agendamento> lista = (clienteId != null)
                ? agendamentoRepository.findByClienteIdComServicos(clienteId)
                : agendamentoRepository.findAllComServicos();
        return lista.stream().map(AgendamentoResponse::from).toList();
    }

    /** Busca um agendamento pelo ID. */
    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        return AgendamentoResponse.from(buscarEntidade(id));
    }

    /**
     * Cria um novo agendamento a partir do DTO do frontend.
     * Calcula valorTotal como soma dos precoBase dos serviços selecionados.
     * Registra evento de auditoria no MongoDB.
     */
    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest req) {
        Veiculo veiculo = veiculoRepository.findById(req.veiculoId())
                .orElseThrow(() -> new NoSuchElementException("Veículo não encontrado: id=" + req.veiculoId()));

        Cliente cliente = veiculo.getCliente();

        Funcionario mecanico = funcionarioRepository.findById(req.mecanicoId())
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado: id=" + req.mecanicoId()));

        List<Servico> servicos = servicoRepository.findAllById(req.servicoIds());
        if (servicos.isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um serviço.");
        }

        double valorTotal = servicos.stream()
                .mapToDouble(Servico::getPrecoBase)
                .sum();

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(LocalDateTime.parse(req.dataHora()));
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setValorTotal(valorTotal);
        agendamento.setCliente(cliente);
        agendamento.setMecanicoResponsavel(mecanico);
        agendamento.setVeiculo(veiculo);
        agendamento.setServicos(servicos);

        AgendamentoResponse resposta = AgendamentoResponse.from(agendamentoRepository.save(agendamento));

        auditoriaService.registrar(
                "CRIAR",
                "Agendamento",
                resposta.id(),
                emailDoUsuarioLogado(),
                "Agendamento criado para veículo id=" + req.veiculoId()
                        + ", mecânico id=" + req.mecanicoId()
                        + ", valor=R$" + valorTotal);

        return resposta;
    }

    /**
     * Atualiza apenas o status de um agendamento.
     * Regras: AGENDADO → EM_MANUTENCAO → CONCLUIDO; qualquer ativo → CANCELADO.
     * Registra evento de auditoria no MongoDB.
     */
    @Transactional
    public AgendamentoResponse atualizarStatus(Long id, String novoStatus) {
        Agendamento agendamento = buscarEntidade(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();
        StatusAgendamento status = StatusAgendamento.valueOf(novoStatus);
        agendamento.setStatus(status);
        AgendamentoResponse resposta = AgendamentoResponse.from(agendamentoRepository.save(agendamento));

        auditoriaService.registrar(
                "ATUALIZAR_STATUS",
                "Agendamento",
                id,
                emailDoUsuarioLogado(),
                "Status alterado de " + statusAnterior + " para " + novoStatus);

        return resposta;
    }

    // --- helpers ---

    private Agendamento buscarEntidade(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado: id=" + id));
    }

    /**
     * Obtém o e-mail do usuário autenticado via SecurityContext. Retorna "ANONIMO"
     * se não houver.
     */
    private String emailDoUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "ANONIMO";
    }
}
