package com.mitocode.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICRUD<T, ID> {

	T registrar(T t);

	T modificar(T t);

	//@PreAuthorize("@restAuthServiceImpl.hasAccess('listar')")
	List<T> listar();
	
	T listarPorId(ID id);

	void eliminar(ID id);
	
	Page<T> listarPageable(Pageable pageable);
}