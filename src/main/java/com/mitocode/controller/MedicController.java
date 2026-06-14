package com.mitocode.controller;

import com.mitocode.dto.MedicDTO;
import com.mitocode.model.Medic;
import com.mitocode.service.IMedicService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/medics")
public class MedicController {
	private final IMedicService service;
	private final ModelMapper mapper;

	//@PreAuthorize("@authServiceImpl.hasAccess('findAll')")
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicDTO>> findAll() {
		val medics = service.findAll().stream().map(this::convertToDto).collect(toList());
		return new ResponseEntity<>(medics, OK);
	}
	
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
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

	@DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id) {
		val medic = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(medic), OK);
	}
	
	@GetMapping(value="/pageableMedic", produces = APPLICATION_JSON_VALUE)
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