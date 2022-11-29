package com.mitocode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name ="paciente")
@SQLDelete(sql = "UPDATE paciente SET estado = false WHERE id_paciente = ?")
@Where(clause = "estado = true")
public class Paciente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPaciente;
	
	@Size(min = 3, message = "{first_name.size}")
	@Column(name = "nombres", nullable = true, length = 70)
	private String nombres;

	@Size(min = 3, message = "{last_name.size}")
	@Column(name = "apellidos", nullable = true, length = 70)
	private String apellidos;

	@Size(min = 9, max = 9, message = "{dui.size}")
	@Column(name = "dui", nullable = false, length = 9)
	private String dui;

	@Size(min = 3, max = 150, message = "{direccion.size}")
	@Column(name = "direccion", length = 150)
	private String direccion;

	@Size(min = 8, max = 8, message = "{telefono.size}")
	@Column(name = "telefono", length = 8)
	private String telefono;
	
	@Email
	@Size(message = "{email.size}")
	@Column(name = "email", length = 150)
	private String email;
	
	@Column(name = "estado")
	private Boolean estado;
}