package com.mitocode.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FilterConsultDTO {

  @Pattern(regexp = "[0-9]+")
  private String dui;
  private String fullName;
  
  @JsonSerialize(using = ToStringSerializer.class)
  private LocalDateTime consultDate;
}