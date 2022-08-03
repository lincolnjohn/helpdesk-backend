package br.thot.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.thot.helpdesk.domain.Chamado;
import br.thot.helpdesk.domain.Cliente;
import br.thot.helpdesk.domain.Tecnico;
import br.thot.helpdesk.domain.dto.ChamadoDto;
import br.thot.helpdesk.domain.enums.Prioridade;
import br.thot.helpdesk.domain.enums.Status;
import br.thot.helpdesk.repositories.ChamadoRepository;
import br.thot.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository repository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	
	public Chamado findById(Integer id) {
		
		Optional<Chamado> obj = repository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: "+id) );
	}

	public List<Chamado> findAll() {
		
		return repository.findAll();
	}

	public Chamado create(@Valid ChamadoDto objDto) {
		return repository.save(newChamado(objDto));
	}
	
	public Chamado update(Integer id, @Valid ChamadoDto objDto) {
		objDto.setId(id);
		Chamado oldObj = findById(id);
		oldObj = newChamado(objDto);
		return repository.save(oldObj);
	}

	
	private Chamado newChamado(ChamadoDto obj) {
		
		Tecnico tecnico = tecnicoService.findById(obj.getTecnico());
		Cliente cliente = clienteService.findById(obj.getCliente());
		
		Chamado chamado = new Chamado();
		if(obj.getId() !=null) {
			chamado.setId(obj.getId());
		}
		
		if(obj.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDate.now());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
		chamado.setStatus(Status.toEnum(obj.getStatus()));
		chamado.setTitulo(obj.getTitulo()) ;
		chamado.setObservacoes(obj.getObservacoes());
		return chamado;
		
	}


}
