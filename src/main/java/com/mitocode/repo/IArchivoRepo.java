package com.mitocode.repo;

import com.mitocode.model.Archivo;
import org.springframework.stereotype.Repository;

@Repository
public interface IArchivoRepo extends IGenericRepo<Archivo, Integer> {
}