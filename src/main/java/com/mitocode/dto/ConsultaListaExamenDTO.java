package com.mitocode.dto;

import com.mitocode.model.Consulta;
import com.mitocode.model.Examen;
import lombok.Data;
import java.util.List;

//Clase auxiliar para insertar en la tabla consulta-detalleConsulta-consultaExamen
@Data
public class ConsultaListaExamenDTO {
	private Consulta consulta;
	private List<Examen> listaExamen;
}