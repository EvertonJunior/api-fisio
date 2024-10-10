package com.ejunior.fisio_api.web.dtos;

import com.ejunior.fisio_api.entities.Care;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CareResponseDto {

    private String physicalTherapistName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime date;
    private String hospitalName;
    private String careName;
    private String patientName;
    private String description;

}
