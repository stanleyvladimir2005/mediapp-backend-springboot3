package com.mitocode.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Data
@Entity
@IdClass(ConsultExamPK.class)
public class ConsultExam {

	@Id
	private Exam exam;

	@Id
	private Consult consult;
}