package com.mitocode.controller;

import com.mitocode.dto.SingDTO;
import com.mitocode.model.Sing;
import com.mitocode.service.ISingService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/sings")
public class SingController {
	
	@Autowired
	private ISingService service;

	@Autowired
	private ModelMapper mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SingDTO>> findAll() {
		List<SingDTO> sings =  service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
		return new ResponseEntity<>(sings, OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@Valid @RequestBody SingDTO singDTO) {
		Sing sing = service.save(convertToEntity(singDTO));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sing.getIdSing()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<SingDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody SingDTO singDTO) {
		singDTO.setIdSing(id);
		Sing cons = service.update(convertToEntity(singDTO),id);
		return new ResponseEntity<>(convertToDto(cons),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SingDTO> findById(@PathVariable("id") Integer id) {
		Sing sing = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(sing), OK);
	}
	
	@GetMapping(value="/pageable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<SingDTO>> listPageable(Pageable pageable) {
		Page<SingDTO> singDTO  = service.listPageable(pageable).map(this::convertToDto);
		return new ResponseEntity<>(singDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<SingDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		Sing sing = service.findById(id);
		SingDTO dtoResponse = convertToDto(sing);
		EntityModel<SingDTO> resource = EntityModel.of(dtoResponse);
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("sing-info1"));
		resource.add(link2.withRel("sing-full"));
		return resource;
	}

	private SingDTO convertToDto(Sing obj){
		return mapper.map(obj, SingDTO.class);
	}

	private Sing convertToEntity(SingDTO dto){
		return mapper.map(dto, Sing.class);
	}
}