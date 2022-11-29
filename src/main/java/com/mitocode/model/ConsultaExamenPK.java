package com.mitocode.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
@Embeddable
public class ConsultaExamenPK implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "id_examen", nullable = false)
	private Examen examen;
	
	@ManyToOne
	@JoinColumn(name = "id_consulta", nullable = false)
	private Consulta consulta;
}