package com.mitocode.service;

import com.mitocode.model.ConsultaExamen;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IConsultaExamenService {

	List<ConsultaExamen> listarExamenesPorConsulta(@Param("idConsulta") Integer idConsulta);
}
