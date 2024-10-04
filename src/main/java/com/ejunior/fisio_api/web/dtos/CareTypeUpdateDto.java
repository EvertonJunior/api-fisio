package com.ejunior.fisio_api.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CareTypeUpdateDto {

    @NotNull
    private Double newPrice;

}
