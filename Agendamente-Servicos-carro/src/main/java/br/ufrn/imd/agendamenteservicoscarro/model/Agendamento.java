package br.ufrn.imd.agendamenteservicoscarro.model;

import br.ufrn.imd.agendamenteservicoscarro.model.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @Column(nullable = false)
    private Double valorTotal = 0.0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario mecanicoResponsavel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToMany
    @JoinTable(name = "agendamento_servico", joinColumns = @JoinColumn(name = "agendamento_id"), inverseJoinColumns = @JoinColumn(name = "servico_id"))
    private List<Servico> servicos = new ArrayList<>();

    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemProduto> itensProduto = new ArrayList<>();

    @OneToOne(mappedBy = "agendamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pagamento pagamento;

}
