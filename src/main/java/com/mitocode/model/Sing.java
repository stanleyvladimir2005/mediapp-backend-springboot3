package com.mitocode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name ="sing")
@SQLDelete(sql = "UPDATE sing SET status = false WHERE id_sing = ?")
@Where(clause = "status = true")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSing;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_patient", nullable = false)
    private Patient patient;

    @JsonSerialize(using = ToStringSerializer.class) //iso date format
    @Column(name = "sing_date")
    private LocalDateTime singDate;

    @Column(name = "temperature", length = 5)
    @Size(min = 3, max = 5, message = "{temperature.size}")
    private String temperature;

    @Column(name = "pulse")
    @Size(min = 1, max = 3, message = "{pulse.size}")
    private String pulse;

    @Size(min = 1, max = 3, message = "{respiratory_rate.size}")
    @Column(name = "respiratory_rate")
    private String respiratoryRate;

    @Column(name = "status")
    private boolean status;
}
