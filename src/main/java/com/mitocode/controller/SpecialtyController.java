package com.mitocode.controller;

import com.mitocode.dto.SpecialtyDTO;
import com.mitocode.model.Specialty;
import com.mitocode.service.ISpecialtyService;
import lombok.val;
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
		val speciality = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
		return new ResponseEntity<>(speciality, OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@Valid @RequestBody SpecialtyDTO SpecialtyDTO) {
		val esp = service.save(convertToEntity(SpecialtyDTO));
		val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(esp.getIdSpecialty()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping ("/{id}")
	public ResponseEntity<SpecialtyDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody SpecialtyDTO specialtyDTO) {
		specialtyDTO.setIdSpecialty(id);
		val spe = service.update(convertToEntity(specialtyDTO),id);
		return new ResponseEntity<>(convertToDto(spe),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id) {
		val specialty = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(specialty), OK);
	}
	
	@GetMapping(value="/pageableSpeciality", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<SpecialtyDTO>> listPageable(Pageable pageable) {
		val specialtyDTO = service.listPageable(pageable).map(this::convertToDto);
		return new ResponseEntity<>(specialtyDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<SpecialtyDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		Specialty esp = service.findById(id);
		val dtoResponse = convertToDto(esp);
		val resource = EntityModel.of(dtoResponse);
		val link1 = linkTo(methodOn(this.getClass()).findById(id));
		val link2 = linkTo(methodOn(this.getClass()).findAll());
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