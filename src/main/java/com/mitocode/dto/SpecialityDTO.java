package com.mitocode.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecialityDTO {

    @EqualsAndHashCode.Include
    private Integer idSpeciality;

    @NotNull
    private String specialityName;

    @NotNull
    private Boolean status;
}