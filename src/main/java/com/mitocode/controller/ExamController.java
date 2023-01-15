package com.mitocode.controller;

import com.mitocode.dto.ExamDTO;
import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Exam;
import com.mitocode.service.IExamService;
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
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/exams")
public class ExamController {
	
	@Autowired
	private IExamService service;

	@Autowired
	private ModelMapper mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ExamDTO>> findAll() {
		List<ExamDTO> exam = service.findAll().stream().map(exa -> mapper.map(exa, ExamDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(exam, OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save (@Valid @RequestBody ExamDTO examDTO) {
		Exam exa = service.save(mapper.map(examDTO, Exam.class));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(exa.getIdExam()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> update(@RequestBody ExamDTO examDTO) {
        Exam exa = service.findById(examDTO.getIdExam());
		if(exa == null)
			throw new ModelNotFoundException("ID NOT FOUND:" +examDTO.getIdExam());

		return new ResponseEntity<>(service.update(mapper.map(examDTO, Exam.class)),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		Exam pac = service.findById(id);
		if (pac == null) 
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else 
			service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id) {
		ExamDTO dtoResponse;
		Exam exam = service.findById(id);
		if (exam == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = mapper.map(exam, ExamDTO.class);

		return new ResponseEntity<>(dtoResponse,OK);
	}
	
	@GetMapping(value="/pageableExam", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ExamDTO>> listPageable(Pageable pageable) {
		Page<ExamDTO> examDTO = service.listPageable(pageable).map(exa -> mapper.map(exa, ExamDTO.class));
		return new ResponseEntity<>(examDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<ExamDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		ExamDTO dtoResponse;
		Exam exa = service.findById(id);
		if (exa == null)
			throw new ModelNotFoundException("ID NOT FOUND: " + id);
		else
			dtoResponse = mapper.map(exa, ExamDTO.class);

		EntityModel<ExamDTO> resource = EntityModel.of(dtoResponse);
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("exam-info1"));
		resource.add(link2.withRel("exam-full"));
		return resource;
	}
}