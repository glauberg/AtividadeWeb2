package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
