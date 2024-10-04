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
@Table(name = "physical_therapists")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PhysicalTherapist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    private Double payment = 0.0;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

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
        PhysicalTherapist that = (PhysicalTherapist) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
