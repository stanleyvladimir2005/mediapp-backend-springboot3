package com.mitocode.controller;

import com.mitocode.dto.SpecialityDTO;
import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Speciality;
import com.mitocode.service.ISpecialityService;
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
@RequestMapping("/v1/specialitys")
public class SpecialityController {
	
	@Autowired
	private ISpecialityService service;

	@Autowired
	private ModelMapper mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SpecialityDTO>> findAll() {
		List<SpecialityDTO> speciality = service.findAll().stream().map(spe -> mapper.map(spe, SpecialityDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(speciality, OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@Valid @RequestBody SpecialityDTO SpecialityDTO) {
		Speciality esp = service.save(mapper.map(SpecialityDTO, Speciality.class));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(esp.getIdSpeciality()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> update(@RequestBody SpecialityDTO specialityDTO) {
		Speciality spe = service.findById(specialityDTO.getIdSpeciality());
		if (spe == null)
			throw new ModelNotFoundException("ID NOT FOUND:" +specialityDTO.getIdSpeciality());

		return new ResponseEntity<>(service.update(mapper.map(specialityDTO, Speciality.class)),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		Speciality spe = service.findById(id);
		if (spe == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else 
			service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SpecialityDTO> findById(@PathVariable("id") Integer id) {
		SpecialityDTO dtoResponse;
		Speciality speciality = service.findById(id);
		if (speciality == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = mapper.map(speciality, SpecialityDTO.class);

		return new ResponseEntity<>(dtoResponse, OK);
	}
	
	@GetMapping(value="/pageableSpeciality", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<SpecialityDTO>> listPageable(Pageable pageable) {
		Page<SpecialityDTO> specialityDTO = service.listPageable(pageable).map(esp -> mapper.map(esp, SpecialityDTO.class));
		return new ResponseEntity<>(specialityDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<SpecialityDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		SpecialityDTO dtoResponse;
		Speciality esp = service.findById(id);
		if (esp == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = mapper.map(esp, SpecialityDTO.class);

		EntityModel<SpecialityDTO> resource = EntityModel.of(dtoResponse);
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("speciality-info1"));
		resource.add(link2.withRel("speciality-full"));
		return resource;
	}
}