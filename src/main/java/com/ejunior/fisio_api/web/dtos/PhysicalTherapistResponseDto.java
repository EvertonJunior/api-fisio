package com.ejunior.fisio_api.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalTherapistResponseDto {

    private Long id;
    private String name;
    private String cpf;

}
