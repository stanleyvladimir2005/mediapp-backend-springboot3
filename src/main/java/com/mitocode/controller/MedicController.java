package com.mitocode.controller;

import com.mitocode.dto.MedicDTO;
import com.mitocode.model.Medic;
import com.mitocode.service.IMedicService;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
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

	//@PreAuthorize("@authServiceImpl.hasAccess('findAll')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicDTO>> findAll() {
		val medics = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
		return new ResponseEntity<>(medics, OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save (@Valid @RequestBody MedicDTO medicDTO) {
		val med = service.save(convertToEntity(medicDTO));
		val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(med.getIdMedic()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<MedicDTO> update(@PathVariable ("id") Integer id,@Valid @RequestBody MedicDTO medicDTO) {
		medicDTO.setIdMedic(id);
		val med = service.update(convertToEntity(medicDTO), id);
		return new ResponseEntity<>(convertToDto(med),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id) {
		val medic = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(medic), OK);
	}
	
	@GetMapping(value="/pageableMedic", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<MedicDTO>> listPageable(Pageable pageable) {
		val medicDTO = service.listPageable(pageable).map(this::convertToDto);
		return new ResponseEntity<>(medicDTO,OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<MedicDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		val med = service.findById(id);
		val dtoResponse = convertToDto(med);
		val resource = EntityModel.of(dtoResponse);
		val link1 = linkTo(methodOn(this.getClass()).findById(id));
		val link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("medic-info1"));
		resource.add(link2.withRel("medic-full"));
		return resource;
	}

	private MedicDTO convertToDto(Medic obj){
		return mapper.map(obj, MedicDTO.class);
	}

	private Medic convertToEntity(MedicDTO dto){
		return mapper.map(dto, Medic.class);
	}
}