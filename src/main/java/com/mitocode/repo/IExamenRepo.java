package com.mitocode.repo;

import com.mitocode.model.Examen;
import org.springframework.stereotype.Repository;

@Repository
public interface IExamenRepo extends IGenericRepo<Examen, Integer> {
}