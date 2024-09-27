package com.ejunior.fisio_api.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter @Setter @AllArgsConstructor@NoArgsConstructor
public class UserPasswordDto {

    private String currentPassword;
    @NotBlank
    @Size(min = 6, max = 10)
    private String newPassword;
    @NotBlank
    @Size(min = 6, max = 10)
    private String confirmNewPassword;

}
