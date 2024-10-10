package com.ejunior.fisio_api.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "invoices")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Invoice extends BaseEntity implements Serializable {


    @Column(name = "date_first_care", nullable = false)
    private LocalDate dateFirstCare;
    @Column(name = "date_last_care", nullable = false)
    private LocalDate dateLastCare;
    @Column(name = "due_date")
    private LocalDate dueDate = LocalDate.now().plusDays(15);
    @ManyToOne
    @JoinColumn(name = "id_hospital")
    private Hospital hospital;
    @Column(name = "total_value")
    private Double totalValue;
    @Column(unique = true,nullable = false,length = 31)
    private String code;
    @OneToOne
    @JoinColumn(name = "id_slip_payment")
    private SlipPayment slipPayment;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.AWAITING_PAYMENT;

    public enum PaymentStatus{
        AWAITING_PAYMENT,
        PAID
    }

}
