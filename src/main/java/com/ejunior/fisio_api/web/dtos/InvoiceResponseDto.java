package com.ejunior.fisio_api.web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class InvoiceResponseDto {

    private long id;
    private String hospitalName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFirstCare;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateLastCare;
    private Double totalValue;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String code;


}
