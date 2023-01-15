package com.mitocode.repo;

import com.mitocode.model.Speciality;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpecialityRepo extends IGenericRepo<Speciality, Integer> {
}