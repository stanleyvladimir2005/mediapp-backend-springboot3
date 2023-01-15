package com.mitocode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "speciality")
@SQLDelete(sql = "UPDATE speciality SET status = false WHERE id_speciality = ?")
@Where(clause = "status = true")
public class Speciality {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idSpeciality;
	
	@Column(name = "speciality_name", nullable = false, length = 50)
	@Size(min=3, message ="{speciality_name.size}")
	private String specialityName;
	
	@Column(name = "status")
	private Boolean status;
}