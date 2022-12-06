package com.mitocode.repo;

import com.mitocode.model.Consulta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IConsultaRepo extends IGenericRepo <Consulta, Integer> {
	
 @Query("from Consulta c where c.paciente.dui =:dui or LOWER(c.paciente.nombres) like %:nombreCompleto% or LOWER(c.paciente.apellidos) like %:nombreCompleto%")
 List<Consulta> buscar(@Param("dui")String dui,@Param("nombreCompleto") String nombreCompleto);

 @Query("from Consulta c where c.fecha between :fechaConsulta and :fechaSgte")
 List<Consulta> buscarFecha(@Param("fechaConsulta") LocalDateTime fechaConsulta, @Param("fechaSgte") LocalDateTime fechaSgte);

 @Query(value = "select * from fn_listarResumen()", nativeQuery = true)
 List<Object[]> listarResumen();
}