package com.mitocode.serviceimpl;

import com.mitocode.model.Patient;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IPatientRepo;
import com.mitocode.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl extends CRUDImpl<Patient, Integer> implements IPatientService {

	private final IPatientRepo repo;

	@Override
	protected IGenericRepo<Patient, Integer> getRepo() {
		return repo;
	}
}