package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.audit.AuditoriaService;
import br.ufrn.imd.agendamenteservicoscarro.dto.AgendamentoRequest;
import br.ufrn.imd.agendamenteservicoscarro.dto.AgendamentoResponse;
import br.ufrn.imd.agendamenteservicoscarro.model.Agendamento;
import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.model.Funcionario;
import br.ufrn.imd.agendamenteservicoscarro.model.ItemProduto;
import br.ufrn.imd.agendamenteservicoscarro.model.Produto;
import br.ufrn.imd.agendamenteservicoscarro.model.Servico;
import br.ufrn.imd.agendamenteservicoscarro.model.Veiculo;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.StatusAgendamento;
import br.ufrn.imd.agendamenteservicoscarro.repository.AgendamentoRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.FuncionarioRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.ProdutoRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.ServicoRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final ProdutoRepository produtoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioLogadoService usuarioLogadoService;

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listar(Long clienteId) {
        Long clienteFiltro = usuarioLogadoService.ehCliente()
                ? usuarioLogadoService.clienteIdObrigatorio()
                : clienteId;

        List<Agendamento> lista;
        if (clienteFiltro != null) {
            lista = agendamentoRepository.findByClienteIdComServicos(clienteFiltro);
        } else if (usuarioLogadoService.ehMecanico()) {
            lista = agendamentoRepository.findByMecanicoResponsavelIdComServicos(
                    usuarioLogadoService.funcionarioIdObrigatorio());
        } else {
            lista = agendamentoRepository.findAllComServicos();
        }

        return lista.stream().map(AgendamentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = buscarEntidade(id);
        validarAcessoCliente(agendamento);
        return AgendamentoResponse.from(agendamento);
    }

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest req) {
        Veiculo veiculo = veiculoRepository.findById(req.veiculoId())
                .orElseThrow(() -> new NoSuchElementException("Veiculo nao encontrado: id=" + req.veiculoId()));

        Cliente cliente = veiculo.getCliente();
        if (usuarioLogadoService.ehCliente()
                && !cliente.getId().equals(usuarioLogadoService.clienteIdObrigatorio())) {
            throw new AccessDeniedException("Cliente so pode agendar para seus proprios veiculos.");
        }

        Funcionario mecanico = funcionarioRepository.findById(req.mecanicoId())
                .orElseThrow(() -> new NoSuchElementException("Funcionario nao encontrado: id=" + req.mecanicoId()));

        List<Servico> servicos = servicoRepository.findAllById(req.servicoIds());
        if (servicos.isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um servico.");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(LocalDateTime.parse(req.dataHora()));
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setCliente(cliente);
        agendamento.setMecanicoResponsavel(mecanico);
        agendamento.setVeiculo(veiculo);
        agendamento.setServicos(servicos);
        agendamento.setItensProduto(criarItensProduto(req, agendamento));

        double valorTotal = servicos.stream().mapToDouble(Servico::getPrecoBase).sum();
        valorTotal += agendamento.getItensProduto().stream()
                .mapToDouble(item -> item.getQuantidade() * item.getPrecoUnitarioHistorico())
                .sum();
        agendamento.setValorTotal(valorTotal);

        AgendamentoResponse resposta = AgendamentoResponse.from(agendamentoRepository.save(agendamento));

        auditoriaService.registrar(
                "CRIAR",
                "Agendamento",
                resposta.id(),
                emailDoUsuarioLogado(),
                "Agendamento criado para veiculo id=" + req.veiculoId()
                        + ", mecanico id=" + req.mecanicoId()
                        + ", valor=R$" + valorTotal);

        return resposta;
    }

    @Transactional
    public AgendamentoResponse atualizarStatus(Long id, String novoStatus) {
        usuarioLogadoService.negarCliente("Cliente nao pode alterar status nem finalizar servico.");
        Agendamento agendamento = buscarEntidade(id);
        validarAcessoMecanico(agendamento);
        StatusAgendamento statusAnterior = agendamento.getStatus();
        StatusAgendamento status = StatusAgendamento.valueOf(novoStatus);
        validarTransicaoStatus(statusAnterior, status);

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

    private Agendamento buscarEntidade(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento nao encontrado: id=" + id));
    }

    private void validarAcessoCliente(Agendamento agendamento) {
        if (usuarioLogadoService.ehCliente()
                && !agendamento.getCliente().getId().equals(usuarioLogadoService.clienteIdObrigatorio())) {
            throw new AccessDeniedException("Cliente so pode acessar seus proprios agendamentos.");
        }
    }

    private void validarAcessoMecanico(Agendamento agendamento) {
        if (usuarioLogadoService.ehMecanico()) {
            Long funcionarioId = usuarioLogadoService.funcionarioIdObrigatorio();
            if (agendamento.getMecanicoResponsavel() == null
                    || !funcionarioId.equals(agendamento.getMecanicoResponsavel().getId())) {
                throw new AccessDeniedException("Mecanico so pode atuar nos atendimentos atribuidos a ele.");
            }
        }
    }

    private List<ItemProduto> criarItensProduto(AgendamentoRequest req, Agendamento agendamento) {
        if (req.produtos() == null || req.produtos().isEmpty()) {
            return List.of();
        }

        return req.produtos().stream()
                .filter(item -> item != null && item.produtoId() != null
                        && item.quantidade() != null && item.quantidade() > 0)
                .map(item -> {
                    Produto produto = produtoRepository.findById(item.produtoId())
                            .orElseThrow(() -> new NoSuchElementException("Produto nao encontrado: id=" + item.produtoId()));

                    if (produto.getEstoque() < item.quantidade()) {
                        throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
                    }

                    produto.setEstoque(produto.getEstoque() - item.quantidade());

                    ItemProduto itemProduto = new ItemProduto();
                    itemProduto.setProduto(produto);
                    itemProduto.setQuantidade(item.quantidade());
                    itemProduto.setPrecoUnitarioHistorico(produto.getPrecoVenda());
                    itemProduto.setAgendamento(agendamento);
                    return itemProduto;
                })
                .toList();
    }

    private void validarTransicaoStatus(StatusAgendamento atual, StatusAgendamento novo) {
        if (atual == novo) {
            return;
        }

        boolean permitido =
                (atual == StatusAgendamento.AGENDADO && novo == StatusAgendamento.EM_MANUTENCAO)
                        || (atual == StatusAgendamento.EM_MANUTENCAO && novo == StatusAgendamento.CONCLUIDO)
                        || ((atual == StatusAgendamento.AGENDADO || atual == StatusAgendamento.EM_MANUTENCAO)
                        && novo == StatusAgendamento.CANCELADO);

        if (!permitido) {
            throw new IllegalArgumentException("Transicao de status invalida: " + atual + " -> " + novo);
        }
    }

    private String emailDoUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "ANONIMO";
    }
}
