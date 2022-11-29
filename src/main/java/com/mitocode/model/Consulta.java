package com.mitocode.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "consulta")
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idConsulta;

	@ManyToOne
	@JoinColumn(name = "id_paciente", nullable = false)
	private Paciente paciente;

	@ManyToOne
	@JoinColumn(name = "id_medico", nullable = false)
	private Medico medico; 

	@ManyToOne
	@JoinColumn(name = "id_especialidad", nullable = false)
	private Especialidad especialidad;

	@JsonSerialize(using = ToStringSerializer.class) //Se agrega el formato ISO DATE 
	private LocalDateTime fecha;

	//Relacion uno a  muchos
	@OneToMany(mappedBy = "consulta", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<DetalleConsulta> detalleConsulta;
}