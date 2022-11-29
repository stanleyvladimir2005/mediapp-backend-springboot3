package com.mitocode.repo;

import com.mitocode.model.Paciente;
import org.springframework.stereotype.Repository;

@Repository
public interface IPacienteRepo extends IGenericRepo<Paciente, Integer> {
}