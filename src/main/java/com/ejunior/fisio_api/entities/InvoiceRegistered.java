package com.ejunior.fisio_api.entities;

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
@Getter
@Setter
public class InvoiceRegistered extends BaseEntity implements Serializable {

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
}