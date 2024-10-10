package com.ejunior.fisio_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class InvoiceRegistered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linhaDigitavel;
    private String qrcodeUrl;
    private String qrcodeEmv;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public InvoiceRegistered create(Invoice invoice, String linhaDigitavel, String qrcodeUrl, String qrcodeEmv) {
        this.linhaDigitavel = linhaDigitavel;
        this.qrcodeUrl = qrcodeUrl;
        this.qrcodeEmv = qrcodeEmv;
        this.invoice = invoice;
        return this;
    }


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
        InvoiceRegistered that = (InvoiceRegistered) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}