package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Patient;
import com.mitocode.service.IPatientService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/patients")
public class PatientController {
	
	@Autowired
	private IPatientService service;

	@Autowired
	private ModelMapper mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PatientDTO>> findAll() {
		List<PatientDTO> patients = service.findAll().stream().map(p -> mapper.map(p, PatientDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(patients, OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@Valid @RequestBody PatientDTO patientDTO) {
		Patient pac = service.save(mapper.map(patientDTO, Patient.class));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pac.getIdPatient()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> update(@RequestBody PatientDTO patientDTO) {
		Patient pac = service.findById(patientDTO.getIdPatient());
		if (pac == null)
			throw new ModelNotFoundException("ID NOT FOUND:" +patientDTO.getIdPatient());

		return new ResponseEntity<>(service.update(mapper.map(patientDTO, Patient.class)),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		Patient pac = service.findById(id);
		if (pac == null) 
			throw new ModelNotFoundException("ID NOT FOUND:" + id);
		else 
			service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id) {
		PatientDTO dtoResponse;
		Patient patient = service.findById(id);
		if (patient == null)
			throw new ModelNotFoundException("ID NOT FOUND:" + id);
        else
			dtoResponse = mapper.map(patient, PatientDTO.class);

		return new ResponseEntity<>(dtoResponse, OK);
	}
	
	@GetMapping(value="/pageablePatient", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<PatientDTO>> listPageable(Pageable pageable) {
		Page<PatientDTO> patientsDTO = service.listPageable(pageable).map(pac -> mapper.map(pac, PatientDTO.class));
		return new ResponseEntity<>(patientsDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<PatientDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		PatientDTO dtoResponse;
		Patient obj = service.findById(id);
		if (obj == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = mapper.map(obj, PatientDTO.class);

		EntityModel<PatientDTO> resource = EntityModel.of(dtoResponse);
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("patient-info1"));
		resource.add(link2.withRel("patient-full"));
		return resource;
	}
}