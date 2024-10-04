package com.ejunior.fisio_api.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PatientCreateDto {

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    @Size(min = 11, max = 11)
    @CPF
    private String cpf;

    @NotNull
    private Long hospId;

}
