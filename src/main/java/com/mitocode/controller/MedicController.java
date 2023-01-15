package com.mitocode.controller;

import com.mitocode.dto.MedicDTO;
import com.mitocode.dto.PatientDTO;
import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Medic;
import com.mitocode.model.Patient;
import com.mitocode.service.IMedicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/medics")
public class MedicController {
	
	@Autowired
	private IMedicService service;

	@Autowired
	private ModelMapper mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicDTO>> findAll() {
		List<MedicDTO> medics = service.findAll().stream().map(m -> mapper.map(m, MedicDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(medics, OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save (@Valid @RequestBody MedicDTO medicDTO) {
		Medic med = service.save(mapper.map(medicDTO, Medic.class));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(med.getIdMedic()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> update(@RequestBody MedicDTO medicDTO) {
		Medic med = service.findById(medicDTO.getIdMedic());
		if (med == null)
			throw new ModelNotFoundException("ID NOT FOUND:" +medicDTO.getIdMedic());
		return new ResponseEntity<>(service.update(mapper.map(medicDTO, Medic.class)),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		Medic med = service.findById(id);
		if (med == null)
			throw new ModelNotFoundException("ID NOT FOUND:" + id);
		else 
			service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id) {
		MedicDTO dtoResponse;
		Medic medic = service.findById(id);
		if (medic == null)
			throw new ModelNotFoundException("Not Found ID: " + id);
		else
			dtoResponse = mapper.map(medic, MedicDTO.class);
		return new ResponseEntity<>(dtoResponse, OK);
	}
	
	@GetMapping(value="/pageableMedic", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Medic>> listPageable(Pageable pageable) {
		Page<Medic> medic = service.listPageable(pageable);
		return new ResponseEntity<>(medic,OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<MedicDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		MedicDTO dtoResponse;
		Medic med = service.findById(id);
		if (med == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = mapper.map(med, MedicDTO.class);

		EntityModel<MedicDTO> resource = EntityModel.of(dtoResponse);
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("patient-info1"));
		resource.add(link2.withRel("patient-full"));
		return resource;
	}
}