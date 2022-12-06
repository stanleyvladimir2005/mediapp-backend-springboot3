package com.mitocode.controller;

import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Examen;
import com.mitocode.service.IExamenService;
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
@RequestMapping("/v1/examenes")
public class ExamenController {
	
	@Autowired
	private IExamenService service;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Examen>> listar() {
		List<Examen> Examenes = service.listar();
		return new ResponseEntity<>(Examenes, HttpStatus.OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrar(@Valid @RequestBody Examen Examen) {
		Examen pac = service.registrar(Examen);
		URI location = ServletUriComponentsBuilder
				       .fromCurrentRequest()
				       .path("/{id}")
					   .buildAndExpand(pac.getIdExamen())
				       .toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizar(@RequestBody Examen Examen) {		
		service.modificar(Examen);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Examen pac = service.listarPorId(id);
		if (pac == null) 
			throw new ModelNotFoundException("ID: " + id);
		else 
			service.eliminar(id);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityModel<Examen> listarId(@PathVariable("id") Integer id) {
		Examen examen = service.listarPorId(id);
		if (examen == null)
			throw new ModelNotFoundException("ID: " + id);	
		
		EntityModel<Examen> entityModel = EntityModel.of(examen);
		Link link= WebMvcLinkBuilder
				   .linkTo(methodOn(this.getClass()).listar())
				   .withRel("all-users");
		entityModel.add(link);
		return entityModel;
	}
	
	@GetMapping(value="/pageableExamen", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Examen>> listarPageable(Pageable pageable) {
		Page<Examen> examenes = service.listarPageable(pageable);
		return new ResponseEntity<>(examenes, HttpStatus.OK);
	}
}