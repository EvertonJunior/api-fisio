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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cares")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Care implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "care_date")
    private Instant date = Instant.now();
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

    @CreatedDate
    @Column(name = "date_creation")
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(name = "date_modification")
    private LocalDateTime modifiedDate;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Care care = (Care) o;
        return Objects.equals(id, care.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
