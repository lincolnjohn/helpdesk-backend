package br.thot.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.thot.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

}
