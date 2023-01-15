package com.mitocode.serviceImpl;

import com.mitocode.model.Speciality;
import com.mitocode.repo.ISpecialityRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.ISpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialityServiceImpl extends CRUDImpl<Speciality,Integer> implements ISpecialityService {

	@Autowired
	private ISpecialityRepo repo;
	
	@Override
	protected IGenericRepo<Speciality, Integer> getRepo() {
		return repo;
	}
}