package com.mitocode.service;

import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.model.Consulta;

import java.util.List;

public interface IConsultaService extends ICRUD <Consulta, Integer> {
	
	Consulta registrarTransaccional(ConsultaListaExamenDTO consultaDTO);
	
	List<Consulta> buscar(FiltroConsultaDTO filtro);

	List<Consulta> buscarFecha(FiltroConsultaDTO filtro);
	
	List<ConsultaResumenDTO> listarResumen();

	byte[] generarReporte() throws Exception ;
}