package br.thot.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.thot.helpdesk.domain.Pessoa;
import br.thot.helpdesk.domain.Tecnico;
import br.thot.helpdesk.domain.dto.TecnicoDto;
import br.thot.helpdesk.repositories.PessoaRepository;
import br.thot.helpdesk.repositories.TecnicoRepository;
import br.thot.helpdesk.services.exceptions.DataIntegrityViolationException;
import br.thot.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		
		return obj.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado Id= "+id) );
	}

	public List<Tecnico> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll()
				;
	}

	public Tecnico create(TecnicoDto objDto) {
		
		objDto.setId(null);
		objDto.setSenha(encoder.encode(objDto.getSenha()));
		validaPorCpfEmail(objDto);

		Tecnico newObj= new Tecnico(objDto);
		
		return repository.save(newObj);
	}
	
	public Tecnico update(Integer id, @Valid TecnicoDto objDto) {
		objDto.setId(id);
		Tecnico oldObj = findById(id);
		validaPorCpfEmail(objDto);
		oldObj = new Tecnico(objDto);
		return repository.save(oldObj);
	}
	
	public void delete(Integer id) {
		
		Tecnico obj = findById(id); 
		
		if(obj.getChamados().size() >0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		}
		
		repository.deleteById(id);
		
		
	}


	private void validaPorCpfEmail(TecnicoDto objDto) {

		Optional<Pessoa> obj=pessoaRepository.findByCpf(objDto.getCpf());
		
		if(obj.isPresent() && obj.get().getId() != objDto.getId()) {
			 throw new DataIntegrityViolationException("CPF já cadastrado no sistema!!");
		}
		
		obj = pessoaRepository.findByEmail(objDto.getEmail());
		
		if(obj.isPresent() && obj.get().getId() != objDto.getId()) {
			 throw new DataIntegrityViolationException("Email já cadastrado no sistema!!");
		}
		
	}


	

}
