package com.mitocode.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SingDTO {

    @EqualsAndHashCode.Include
    private Integer idSing;

    @JsonBackReference
    private PatientDTO patient;

    @NotNull
    private LocalDateTime singDate;

    @NotNull
    private String temperature;

    @NotNull
    private String pulse;

    @NotNull
    private String respiratoryRate;

    @NotNull
    private boolean status;
}
