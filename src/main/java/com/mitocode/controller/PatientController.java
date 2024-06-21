package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.model.Patient;
import com.mitocode.service.IPatientService;
import jakarta.validation.Valid;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/patients")
public class PatientController {
	
	@Autowired
	private IPatientService service;

	@Autowired
	private ModelMapper mapper;

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PatientDTO>> findAll() {
		val patients = service.findAll().stream().map(this::convertToDto).collect(toList());
		return new ResponseEntity<>(patients, OK);
	}
	
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@Valid @RequestBody PatientDTO patientDTO) {
		val pac = service.save(convertToEntity(patientDTO));
		val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pac.getIdPatient()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<PatientDTO> update(@PathVariable("id") Integer id, @Valid @RequestBody PatientDTO patientDTO) {
		patientDTO.setIdPatient(id);
		val pac = service.update(convertToEntity(patientDTO), id);
		return new ResponseEntity<>(convertToDto(pac),OK);
	}

	@DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id) {
		val patient = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(patient), OK);
	}
	
	@GetMapping(value="/pageablePatient", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<PatientDTO>> listPageable(Pageable pageable) {
		val patientsDTO = service.listPageable(pageable).map(this::convertToDto);
		return new ResponseEntity<>(patientsDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<PatientDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		val pac = service.findById(id);
		val dtoResponse = convertToDto(pac);
		val resource = EntityModel.of(dtoResponse);
		val link1 = linkTo(methodOn(this.getClass()).findById(id));
		val link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("patient-info1"));
		resource.add(link2.withRel("patient-full"));
		return resource;
	}

	private PatientDTO convertToDto(Patient obj){
		return mapper.map(obj, PatientDTO.class);
	}

	private Patient convertToEntity(PatientDTO dto){
		return mapper.map(dto, Patient.class);
	}
}