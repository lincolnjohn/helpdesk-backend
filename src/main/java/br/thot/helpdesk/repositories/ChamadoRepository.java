package br.thot.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.thot.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

}
