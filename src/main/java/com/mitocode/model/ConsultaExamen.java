package com.mitocode.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(ConsultaExamenPK.class)
public class ConsultaExamen {

	@Id
	private Examen examen;

	@Id
	private Consulta consulta;
}