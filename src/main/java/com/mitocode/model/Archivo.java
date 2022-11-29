package com.mitocode.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "archivo")
public class Archivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idArchivo;
	private String filename;
	private String filetype;
	private byte[] value;
}