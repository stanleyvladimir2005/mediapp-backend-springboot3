package com.mitocode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table (name = "examen")
@SQLDelete(sql = "UPDATE examen SET estado = false WHERE id_examen = ?")
@Where(clause = "estado = true")
public class Examen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idExamen;
	
	@Column(name="nombre", nullable= false, length=50)
	@Size(min=3, message="{examen_name.size}")
	private String nombre;
	
	@Column(name="descripcion", nullable= false, length=150)
	@Size(min=3, message="{examen_descripcion.size}")
	private String descripcion;
	
	@Column(name = "estado")
	private Boolean estado;
}