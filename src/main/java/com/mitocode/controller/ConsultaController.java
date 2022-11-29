package com.mitocode.controller;

import com.mitocode.dto.ConsultaDTO;
import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.model.Archivo;
import com.mitocode.model.Consulta;
import com.mitocode.service.IArchivoService;
import com.mitocode.service.IConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/consultas")
public class ConsultaController {
	
	@Autowired
	private IConsultaService service;
	
	@Autowired
	private IArchivoService serviceArchivo;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Consulta>> listar() {
		List<Consulta> consultas =  service.listar();
		return new ResponseEntity<>(consultas, HttpStatus.OK);
	}
		
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrar(@Valid @RequestBody ConsultaListaExamenDTO Consulta) {
		Consulta obj = service.registrarTransaccional(Consulta);
		URI location = ServletUriComponentsBuilder
				          .fromCurrentRequest()
				          .path("/{id}")
					      .buildAndExpand(obj.getIdConsulta())
				          .toUri();
		return ResponseEntity.created(location).build();			
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizar(@RequestBody Consulta Consulta) {		
		service.modificar(Consulta);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Consulta obj = service.listarPorId(id);
		if (obj == null) 
			throw new ModelNotFoundException("ID: " + id);
		else 
			service.eliminar(id);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityModel<Consulta> listarId(@PathVariable("id") Integer id) {
		Consulta consulta = service.listarPorId(id);
		if (consulta == null)
			throw new ModelNotFoundException("ID: " + id);	
		
		EntityModel<Consulta> entityModel = EntityModel.of(consulta);
		Link link= WebMvcLinkBuilder
				     .linkTo(methodOn(this.getClass()).listar())
				     .withRel("all-users");
		entityModel.add(link);
		return entityModel;
	}
	
	@GetMapping(value="/pageable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Consulta>> listarPageable(Pageable pageable) {
		Page<Consulta> consulta  = service.listarPageable(pageable);
		return new ResponseEntity<>(consulta, HttpStatus.OK);
	}
	
	//Nivel 3 Richardson
	@GetMapping(value = "/hateoas", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ConsultaDTO> listarHateoas() {
		List<Consulta> consultas;
		List<ConsultaDTO> consultasDTO = new ArrayList<>();
		consultas = service.listar();

		for (Consulta c : consultas) {
			ConsultaDTO d = new ConsultaDTO();
			d.setIdConsulta(c.getIdConsulta());
			d.setMedico(c.getMedico());
			d.setPaciente(c.getPaciente());

			WebMvcLinkBuilder linkTo = linkTo(methodOn(ConsultaController.class).listarId((c.getIdConsulta())));
			d.add(linkTo.withSelfRel());

			WebMvcLinkBuilder linkTo1 = linkTo(methodOn(PacienteController.class).listarId((c.getPaciente().getIdPaciente())));
			d.add(linkTo1.withSelfRel());

			WebMvcLinkBuilder linkTo2 = linkTo(methodOn(MedicoController.class).listarId((c.getMedico().getIdMedico())));
			d.add(linkTo2.withSelfRel());
			consultasDTO.add(d);
		}
		return consultasDTO;
	}
	
	@PostMapping("/buscar")
	public ResponseEntity<List<Consulta>> buscar(@RequestBody FiltroConsultaDTO filtro) {
		List<Consulta> consultas = null;

		if (filtro != null) {
			if (filtro.getFechaConsulta() != null) 
				consultas = service.buscarFecha(filtro);
			 else 
				consultas = service.buscar(filtro);
		}
		return new ResponseEntity<>(consultas, HttpStatus.OK);
	}
	
	@GetMapping(value = "/listarResumen")
	public ResponseEntity<List<ConsultaResumenDTO>> listarResumen() {
		List<ConsultaResumenDTO> consultas  = service.listarResumen();
		return new ResponseEntity<>(consultas, HttpStatus.OK);
	}
	
	@GetMapping(value = "/generarReporte", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReporte() throws Exception {
		byte[] data = service.generarReporte();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@PostMapping(value = "/guardarArchivo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> guardarArchivo(@RequestParam("file") MultipartFile file) throws IOException{
		Archivo ar = new Archivo();
		ar.setFiletype(file.getContentType());
		ar.setFilename(file.getName());
		ar.setValue(file.getBytes());
		serviceArchivo.registrar(ar);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/leerArchivo/{idArchivo}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> leerArchivo(@PathVariable("idArchivo") Integer idArchivo) {
		byte[] arr = serviceArchivo.listarPorId(idArchivo).getValue();
		return new ResponseEntity<>(arr, HttpStatus.OK);
	}
}