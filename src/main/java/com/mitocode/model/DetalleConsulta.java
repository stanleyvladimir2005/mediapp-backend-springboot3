package com.mitocode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_consulta")
public class DetalleConsulta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDetalle;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_consulta", nullable = false)
	private Consulta consulta;

	@Column(name = "diagnostico", nullable = false, length = 70)
	private String diagnostico;

	@Column(name = "tratamiento", nullable = false, length = 300)
	private String tratamiento;
}