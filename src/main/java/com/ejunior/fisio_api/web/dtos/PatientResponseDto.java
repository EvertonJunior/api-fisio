package com.ejunior.fisio_api.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {

    private Long id;
    private String name;
    private String cpf;
    private String hospitalName;

}
