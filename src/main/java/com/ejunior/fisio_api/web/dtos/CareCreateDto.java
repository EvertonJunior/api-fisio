package com.ejunior.fisio_api.web.dtos;

import com.ejunior.fisio_api.entities.Care;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.core.Transient;


@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class CareCreateDto {

    @NotNull
    private Long hospitalId;
    @NotNull
    private Long patientId;
    @NotNull
    private Long careTypeId;
    @NotNull
    @Size(min = 5, max = 300)
    private String description;

}
