package br.thot.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.thot.helpdesk.domain.Pessoa;
import br.thot.helpdesk.domain.Cliente;
import br.thot.helpdesk.domain.dto.ClienteDto;
import br.thot.helpdesk.repositories.PessoaRepository;
import br.thot.helpdesk.repositories.ClienteRepository;
import br.thot.helpdesk.services.exceptions.DataIntegrityViolationException;
import br.thot.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		
		return obj.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado Id= "+id) );
	}

	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	public Cliente create(ClienteDto objDto) {
		
		objDto.setId(null);
		objDto.setSenha(encoder.encode(objDto.getSenha()));
		validaPorCpfEmail(objDto);

		Cliente newObj= new Cliente(objDto);
		
		return repository.save(newObj);
	}
	
	public Cliente update(Integer id, @Valid ClienteDto objDto) {
		objDto.setId(id);
		Cliente oldObj = findById(id);
		validaPorCpfEmail(objDto);
		oldObj = new Cliente(objDto);
		return repository.save(oldObj);
	}
	
	public void delete(Integer id) {
		
		Cliente obj = findById(id); 
		
		if(obj.getChamados().size() >0) {
			throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
		}
		
		repository.deleteById(id);
		
		
	}


	private void validaPorCpfEmail(ClienteDto objDto) {

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
