package com.mitocode.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FiltroConsultaDTO {

  private String dui;  
  private String nombreCompleto;
  
  @JsonSerialize(using = ToStringSerializer.class)
  private LocalDateTime fechaConsulta;
}