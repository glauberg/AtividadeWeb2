package br.ufrn.imd.agendamenteservicoscarro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Cliente extends Pessoa {

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Veiculo> veiculos = new ArrayList<>();

}
