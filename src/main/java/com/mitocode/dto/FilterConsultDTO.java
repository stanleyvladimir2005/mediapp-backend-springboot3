package com.mitocode.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FilterConsultDTO {

  private String dui;  
  private String fullName;
  
  @JsonSerialize(using = ToStringSerializer.class)
  private LocalDateTime consultDate;
}