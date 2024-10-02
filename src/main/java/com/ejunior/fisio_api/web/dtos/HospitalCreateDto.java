package com.ejunior.fisio_api.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class HospitalCreateDto {

    @NotBlank
    @Size(min = 6, max = 100)
    private String name;
    @NotBlank
    @Size(min = 14, max = 14)
    @CNPJ
    private String cnpj;

}
