package com.ejunior.fisio_api.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "hospitals")
public class Hospital extends BaseEntity implements Serializable {

    @Column(nullable = false, length = 100)
    private String name;
    @Column(unique = true, nullable = false, length = 14)
    private String cnpj;

    @Embedded
    private Address address;
}