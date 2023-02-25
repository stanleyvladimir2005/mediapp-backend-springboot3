package com.mitocode.model;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Data
@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idRole;

	@Column(length = 50)
	@Size(min = 3, message = "{user.name}")
	private String name;

	@Column(nullable = false, length = 150)
	private String description;
}