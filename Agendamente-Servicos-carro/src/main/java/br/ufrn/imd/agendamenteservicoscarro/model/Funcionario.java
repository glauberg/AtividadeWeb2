package br.ufrn.imd.agendamenteservicoscarro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funcionario")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Funcionario extends Pessoa {

    // Funcionario herda os atributos de Pessoa
    // discutir com bruno e glauber sobre os atributos a mais

}
