package com.mitocode.repo;

import com.mitocode.model.Medico;
import org.springframework.stereotype.Repository;

@Repository
public interface IMedicoRepo extends IGenericRepo<Medico, Integer> {
}