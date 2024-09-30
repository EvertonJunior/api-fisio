package com.ejunior.fisio_api.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PhysicalTherapistCreateDto {


    @NotBlank
    @Size(min = 6, max = 100)
    private String name;

    @NotBlank
    @Size(min = 11, max = 11)
    @CPF
    private String cpf;

}
