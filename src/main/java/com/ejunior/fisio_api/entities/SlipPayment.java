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
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SlipPayment extends BaseEntity implements Serializable {

    @Column(unique = true, length = 5)
    private String numeroDocumento;
    private String nossoNumero;
    private String numeroBanco = "001";
    private String numeroAgencia = "452";
    private String numeroConta = "123873";
    private String digitoAgencia = "x";
    private String digitoConta = "5";
    private String razaoSocialNossaEmpresa = "JE Fisioterpia Ltda.";
    private String cnpjNossaEmpresa = "10169408000145";
    @Embedded
    private Address address;
    private String numeroContrato = "3128557";
    private String carteira = "17";
    private String variacaoCarteira = "35";
    private String jurosPorcentagem = "1";
    private String multaPorcentagem = "2";

}
