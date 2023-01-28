package com.mitocode.controller;

import com.mitocode.dto.SpecialtyDTO;
import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Specialty;
import com.mitocode.service.ISpecialtyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/specialtys")
public class SpecialtyController {
	
	@Autowired
	private ISpecialtyService service;

	@Autowired
	private ModelMapper mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SpecialtyDTO>> findAll() {
		List<SpecialtyDTO> speciality = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
		return new ResponseEntity<>(speciality, OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@Valid @RequestBody SpecialtyDTO SpecialtyDTO) {
		Specialty esp = service.save(convertToEntity(SpecialtyDTO));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(esp.getIdSpecialty()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> update(@Valid @RequestBody SpecialtyDTO specialtyDTO) {
		Specialty spe = service.findById(specialtyDTO.getIdSpecialty());
		if (spe == null)
			throw new ModelNotFoundException("ID NOT FOUND:" + specialtyDTO.getIdSpecialty());

		return new ResponseEntity<>(service.update(convertToEntity(specialtyDTO)),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		Specialty spe = service.findById(id);
		if (spe == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else 
			service.delete(id);

		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id) {
		SpecialtyDTO dtoResponse;
		Specialty specialty = service.findById(id);
		if (specialty == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = convertToDto(specialty);

		return new ResponseEntity<>(dtoResponse, OK);
	}
	
	@GetMapping(value="/pageableSpeciality", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<SpecialtyDTO>> listPageable(Pageable pageable) {
		Page<SpecialtyDTO> specialtyDTO = service.listPageable(pageable).map(this::convertToDto);
		return new ResponseEntity<>(specialtyDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<SpecialtyDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		SpecialtyDTO dtoResponse;
		Specialty esp = service.findById(id);
		if (esp == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = convertToDto(esp);

		EntityModel<SpecialtyDTO> resource = EntityModel.of(dtoResponse);
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("specialty-info1"));
		resource.add(link2.withRel("specialty-full"));
		return resource;
	}

	private SpecialtyDTO convertToDto(Specialty obj){
		return mapper.map(obj, SpecialtyDTO.class);
	}

	private Specialty convertToEntity(SpecialtyDTO dto){
		return mapper.map(dto, Specialty.class);
	}
}