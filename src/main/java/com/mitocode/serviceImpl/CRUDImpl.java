package com.mitocode.serviceImpl;

import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.ICRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public abstract class CRUDImpl<T,ID> implements ICRUD<T, ID> {
	
	protected abstract IGenericRepo<T, ID> getRepo();

	public T save(T t){
		return getRepo().save(t);		
	}

	public T update(T t){
		return getRepo().save(t);
	}
	
	public List<T> findAll(){
		return getRepo().findAll();
	}

	public T findById(ID id) {
		return getRepo().findById(id).orElse(null);
	}
	
	public void delete(ID id) { getRepo().deleteById(id);	}
	
	public Page<T> listPageable(Pageable pageable){
		return getRepo().findAll(pageable);
	}
}