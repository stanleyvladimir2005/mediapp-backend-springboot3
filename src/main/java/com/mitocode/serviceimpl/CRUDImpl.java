package com.mitocode.serviceimpl;

import com.mitocode.exceptions.ModelNotFoundException;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.ICRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public abstract class CRUDImpl<T,ID> implements ICRUD<T, ID> {
	
	protected abstract IGenericRepo<T, ID> getRepo();
	private static final String NOT_FOUND = "ID NOT FOUND: ";

	public T save(T t){
		return getRepo().save(t);		
	}

	public T update(T t, ID id){
 		getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException(NOT_FOUND+id));
		return getRepo().save(t);
	}
	
	public List<T> findAll(){
		return getRepo().findAll();
	}

	public T findById(ID id) {
		return getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException(NOT_FOUND+id));
	}
	
	public void delete(ID id) {
		getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException(NOT_FOUND+id));
		getRepo().deleteById(id);
	}
	
	public Page<T> listPageable(Pageable pageable){
		return getRepo().findAll(pageable);
	}
}