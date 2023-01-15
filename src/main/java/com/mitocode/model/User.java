package com.mitocode.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user_data")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUser;

	@Column(name = "user_name", nullable = false, unique = true)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_rol", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "idUser"),
			inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "idRol"))
	private List<Rol> roles;
}