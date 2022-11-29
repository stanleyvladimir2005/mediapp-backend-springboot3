package com.mitocode.controller;

import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Medico;
import com.mitocode.service.IMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/medicos")
public class MedicoController {
	
	@Autowired
	private IMedicoService service;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Medico>> listar() {
		List<Medico> Medicos = service.listar();
		return new ResponseEntity<>(Medicos, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrar(@Valid @RequestBody Medico Medico) {
		Medico med = service.registrar(Medico);
		URI location = ServletUriComponentsBuilder
				         .fromCurrentRequest()
				          .path("/{id}")
					     .buildAndExpand(med.getIdMedico())
				         .toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizar(@RequestBody Medico Medico) {		
		service.modificar(Medico);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Medico pac = service.listarPorId(id);
		if (pac == null) 
			throw new ModelNotFoundException("ID: " + id);
		else 
			service.eliminar(id);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityModel<Medico> listarId(@PathVariable("id") Integer id) {
		Medico Medico = service.listarPorId(id);
		if (Medico == null) 
			throw new ModelNotFoundException("ID: " + id);	
		
		EntityModel<Medico> entityModel = EntityModel.of(Medico);
		Link link= WebMvcLinkBuilder
				   .linkTo(methodOn(this.getClass()).listar())
				   .withRel("all-users");
		entityModel.add(link);
		return entityModel;
	}
	
	@GetMapping(value="/pageableMedico", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Medico>> listarPageable(Pageable pageable) {
		Page<Medico> Medicos = service.listarPageable(pageable);
		return new ResponseEntity<>(Medicos, HttpStatus.OK);
	}
}