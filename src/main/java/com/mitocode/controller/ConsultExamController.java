package com.mitocode.controller;

import com.mitocode.dto.ConsultExamDTO;
import com.mitocode.service.IConsultExamService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/consult-exam")
public class ConsultExamController {
	private final IConsultExamService service;
	private final ModelMapper mapper;

	@GetMapping(value = "/{idConsult}")
	public ResponseEntity<List<ConsultExamDTO>> getConsultsById(@PathVariable("idConsult") Integer idConsult) {
		val consultaExamen = service.listExamByConsult(idConsult);
		List<ConsultExamDTO> lstDTO = mapper.map(consultaExamen, new TypeToken<List<ConsultExamDTO>>() {}.getType());
		return new ResponseEntity<>(lstDTO, OK);
	}
}