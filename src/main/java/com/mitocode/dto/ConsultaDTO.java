package com.mitocode.dto;

import com.mitocode.model.Medico;
import com.mitocode.model.Paciente;
import org.springframework.hateoas.EntityModel;

//Clase auxiliar para crear el HATEOAS nivel 3 a la consulta
public class ConsultaDTO extends EntityModel<Object> {
	private Integer idConsulta;
	private Medico medico;
	private Paciente paciente;

	public int getIdConsulta() {return idConsulta;}

	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
}