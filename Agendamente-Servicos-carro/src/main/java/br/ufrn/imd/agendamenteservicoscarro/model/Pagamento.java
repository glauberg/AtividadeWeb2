package br.ufrn.imd.agendamenteservicoscarro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.MetodoPagamento;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPagamento metodo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    private LocalDateTime dataPagamento;

    @Column(nullable = false)
    private Double valorPago;

    @JsonIgnore
    @OneToOne(optional = false)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

}
