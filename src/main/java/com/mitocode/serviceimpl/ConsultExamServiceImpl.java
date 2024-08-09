package com.mitocode.serviceimpl;

import com.mitocode.model.ConsultExam;
import com.mitocode.repo.IConsultExamRepo;
import com.mitocode.service.IConsultExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultExamServiceImpl implements IConsultExamService {

	private final IConsultExamRepo repo;

	@Override
	public List<ConsultExam> listExamByConsult(Integer idConsult) {
		return repo.listExamByConsult(idConsult);
	}
}