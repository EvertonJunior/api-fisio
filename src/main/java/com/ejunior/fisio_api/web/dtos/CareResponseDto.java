package com.ejunior.fisio_api.web.dtos;

import com.ejunior.fisio_api.entities.Care;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CareResponseDto {

    private String physicalTherapistName;
    private String hospitalName;
    private String careName;
    private String patientName;
    private String description;

}
