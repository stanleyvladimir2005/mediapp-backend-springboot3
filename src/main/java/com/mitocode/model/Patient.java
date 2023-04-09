package com.mitocode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Data
@Entity
@Table(name ="patient")
@SQLDelete(sql = "UPDATE patient SET status = false WHERE id_patient = ?")
@Where(clause = "status = true")
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPatient;
	
	@Size(min = 3, message = "{first_name.size}")
	@Column(name = "first_name", length = 70)
	private String firstName;

	@Size(min = 3, message = "{last_name.size}")
	@Column(name = "last_name", length = 70)
	private String lastName;

	@Size(min = 9, max = 9, message = "{dui.size}")
	@Column(name = "dui", nullable = false, length = 9)
	private String dui;

	@Size(min = 3, max = 150, message = "{direccion.size}")
	@Column(name = "address", length = 150)
	private String address;

	@Size(min = 8, max = 8, message = "{telefono.size}")
	@Column(name = "phone", length = 8)
	private String phone;
	
	@Email
	@Size(message = "{email.size}")
	@Column(name = "email", length = 150)
	private String email;

	@OneToMany(mappedBy = "patient", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Sing> singList;
	
	@Column(name = "status")
	private Boolean status;
}