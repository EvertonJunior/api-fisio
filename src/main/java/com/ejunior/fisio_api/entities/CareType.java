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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "care_types")
public class CareType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 4)
    private String code;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private Double price;

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
        CareType careType = (CareType) o;
        return Objects.equals(id, careType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
