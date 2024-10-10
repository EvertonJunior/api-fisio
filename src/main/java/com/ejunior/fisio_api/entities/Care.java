package com.ejunior.fisio_api.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cares")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Care extends BaseEntity implements Serializable {


    @Column(name = "care_date")
    private LocalDateTime date = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "id_care_type")
    private CareType type;
    private String name;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "id_physical_therapist")
    private PhysicalTherapist physicalTherapist;
    @ManyToOne
    @JoinColumn(name = "id_hospital")
    private Hospital hospital;
    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;
    @Column(nullable = false, length = 300)
    private String description;
}
