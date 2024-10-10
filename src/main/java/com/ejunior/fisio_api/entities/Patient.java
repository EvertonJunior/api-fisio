package com.ejunior.fisio_api.entities;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "patients")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Patient extends BaseEntity implements Serializable {

    @Column(nullable = false, length = 100)
    private String name;
    @Column(unique = true, nullable = false,length = 11)
    private String cpf;
    @ManyToOne
    @JoinColumn(name = "id_hospital")
    private Hospital hospital;

}
