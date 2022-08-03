package br.thot.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.thot.helpdesk.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
