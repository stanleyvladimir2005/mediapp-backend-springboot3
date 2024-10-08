package com.mitocode.controller;

import com.mitocode.dto.*;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.model.MediaFile;
import com.mitocode.service.IMediaFileService;
import com.mitocode.service.IConsultService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/consults")
public class ConsultController {
	private final IConsultService service;
	private final IMediaFileService mediaFileService;
	private final ModelMapper mapper;

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ConsultDTO>> findAll() {
		val consults =  service.findAll().stream().map(this::convertToDto).collect(toList());
		return new ResponseEntity<>(consults, OK);
	}
		
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(@Valid @RequestBody ConsultListExamDTO dto) {
		val c = convertToEntity(dto.getConsult());
		List<Exam> exams = mapper.map(dto.getListExam(), new TypeToken<List<Exam>>() {}.getType());
		val obj = service.saveTransactional(c, exams);
		val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult()).toUri();
		return ResponseEntity.created(location).build();			
	}

	@PutMapping("/{id}")
	public ResponseEntity<ConsultDTO> update(@PathVariable("id") Integer id,@Valid @RequestBody ConsultDTO consultDTO) {
		consultDTO.setIdConsult(id);
		val cons = service.update(convertToEntity(consultDTO),id);
		return new ResponseEntity<>(convertToDto(cons),OK);
	}

	@DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ConsultDTO> findById(@PathVariable("id") Integer id) {
		val consult = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(consult), OK);
	}
	
	@GetMapping(value="/pageable", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ConsultDTO>> listPageable(Pageable pageable) {
		val consultDTO  = service.listPageable(pageable).map(this::convertToDto);
		return new ResponseEntity<>(consultDTO, OK);
	}

	@GetMapping(value = "/hateoas", produces = APPLICATION_JSON_VALUE)
	public List<ConsultHateoasDTO> listHateoas() {
		val consults = service.findAll();
		return consults.stream()
				.map(c -> {
					val d = new ConsultHateoasDTO();
					d.setIdConsult(c.getIdConsult());
					d.setMedic(c.getMedic());
					d.setPatient(c.getPatient());

					val linkTo = linkTo(methodOn(ConsultController.class).findById(c.getIdConsult()));
					d.add(linkTo.withSelfRel());

					val linkToPatient = linkTo(methodOn(PatientController.class).findById(c.getPatient().getIdPatient()));
					d.add(linkToPatient.withRel("patient"));

					val linkToMedic = linkTo(methodOn(MedicController.class).findById(c.getMedic().getIdMedic()));
					d.add(linkToMedic.withRel("medic"));
					return d;
				})
				.collect(toList());
	}


	@PostMapping("/search/others")
	public ResponseEntity<List<ConsultDTO>> searchByOthers(@RequestBody FilterConsultDTO filterDTO){
		val consults = service.search(filterDTO.getDui(), filterDTO.getFullname());
		List<ConsultDTO> consultsDTO = mapper.map(consults, new TypeToken<List<ConsultDTO>>() {}.getType());
		return new ResponseEntity<>(consultsDTO, OK);
	}

	@GetMapping("/search/date")
	public ResponseEntity<List<ConsultDTO>> searchByDates(@RequestParam(value = "date1") String date1,
														  @RequestParam(value = "date2") String date2){
		val consults = service.searchByDates(LocalDateTime.parse(date1), LocalDateTime.parse(date2));
		List<ConsultDTO> consultsDTO = mapper.map(consults, new TypeToken<List<ConsultDTO>>() {}.getType());
		return new ResponseEntity<>(consultsDTO, OK);
	}
	
	@GetMapping(value = "/callProcedure")
	public ResponseEntity<List<ConsultProductDTO>> listProducts() {
		val consults  = service.callProcedureOrFunction();
		return new ResponseEntity<>(consults, OK);
	}
	
	@GetMapping(value = "/generateReport", produces = APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generateReport() throws Exception {
		val data = service.generateReport();
		return new ResponseEntity<>(data, OK);
	}
	
	@PostMapping(value = "/saveFile", consumes = {MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> saveFile(@RequestParam("file") MultipartFile file) throws IOException{
		val mf = new MediaFile();
		mf.setFileType(file.getContentType());
		mf.setFileName(file.getName());
		mf.setValue(file.getBytes());
		mediaFileService.save(mf);
		return new ResponseEntity<>(OK);
	}
	
	@GetMapping(value = "/readFile/{idFile}", produces = APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> readFile(@PathVariable("idFile") Integer idFile) {
		val arr = mediaFileService.findById(idFile).getValue();
		return new ResponseEntity<>(arr, OK);
	}

	private ConsultDTO convertToDto(Consult obj){
		return mapper.map(obj, ConsultDTO.class);
	}

	private Consult convertToEntity(ConsultDTO dto){
		return mapper.map(dto, Consult.class);
	}
}