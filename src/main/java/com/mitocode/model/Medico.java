package com.mitocode.model;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
@Entity
@Table(name="medicos")
@SQLDelete(sql = "UPDATE medicos SET estado = false WHERE id_medico = ?")
@Where(clause = "estado = true")
public class Medico {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idMedico;

	@Size(min = 3, message = "{first_name.size}")
	@Column(name = "nombres", nullable = false, length = 70)
	private String nombres;

	@Size(min = 3, message = "{last_name.size}")
	@Column(name = "apellidos", nullable = false, length = 70)
	private String apellidos;

	@Size(min = 9, max = 9, message = "{dui.size}")
	@Column(name = "dui", nullable = false, length = 9)
	private String dui;

	@Size(min = 8, max = 8, message = "{telefono.size}")
	@Column(name = "telefono", nullable = false, length = 8)
	private String telefono;
	
	@Email
	@Size(message = "Email debe hasta 150 caracteres en formato xxxxx@xxxxx.com")
	@Column(name = "email", length = 150)
	private String email;

	@Column(name = "photoUrl")
	private String photoUrl;
	
	@Column(name = "estado")
	private Boolean estado;
}