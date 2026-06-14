package com.mitocode.controller;

import com.mitocode.dto.ExamDTO;
import com.mitocode.model.Exam;
import com.mitocode.service.IExamService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/exams")
public class ExamController {
	private final IExamService service;
	private final ModelMapper mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ExamDTO>> findAll() {
		val exam = service.findAll().stream().map(this::convertToDto).collect(toList());
		return new ResponseEntity<>(exam, OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save (@Valid @RequestBody ExamDTO examDTO) {
		val exa = service.save(convertToEntity(examDTO));
		val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(exa.getIdExam()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<ExamDTO> update(@PathVariable("id") Integer id, @Valid @RequestBody ExamDTO examDTO) {
        examDTO.setIdExam(id);
		val exa = service.update(convertToEntity(examDTO), id);
		return new ResponseEntity<>(convertToDto(exa),OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id) {
		val exam = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(exam),OK);
	}
	
	@GetMapping(value="/pageableExam", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ExamDTO>> listPageable(Pageable pageable) {
		val examDTO = service.listPageable(pageable).map(this::convertToDto);
		return new ResponseEntity<>(examDTO, OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<ExamDTO> findByIdHateoas(@PathVariable("id") Integer id) {
		val exa = service.findById(id);
		val dtoResponse = convertToDto(exa);
		val resource = EntityModel.of(dtoResponse);
		val link1 = linkTo(methodOn(this.getClass()).findById(id));
		val link2 = linkTo(methodOn(this.getClass()).findAll());
		resource.add(link1.withRel("exam-info1"));
		resource.add(link2.withRel("exam-full"));
		return resource;
	}

	private ExamDTO convertToDto(Exam obj){
		return mapper.map(obj, ExamDTO.class);
	}

	private Exam convertToEntity(ExamDTO dto){
		return mapper.map(dto, Exam.class);
	}
}