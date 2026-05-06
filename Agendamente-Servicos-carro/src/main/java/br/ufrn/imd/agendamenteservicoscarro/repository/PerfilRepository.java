package br.ufrn.imd.agendamenteservicoscarro.repository;

import br.ufrn.imd.agendamenteservicoscarro.model.Perfil;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.PerfilNome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByNome(PerfilNome nome);
}
