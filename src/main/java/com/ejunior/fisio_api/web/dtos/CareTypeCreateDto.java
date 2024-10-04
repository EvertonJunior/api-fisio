package com.ejunior.fisio_api.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CareTypeCreateDto {

    @NotBlank
    @Size(min = 4, max =4)
    private String code;
    @NotBlank
    @Size(min = 5, max =100)
    private String name;
    @NotNull
    private Double price;

}
