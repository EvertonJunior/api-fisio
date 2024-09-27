package com.ejunior.fisio_api.web.dtos;


import com.ejunior.fisio_api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Setter @Getter
public class UserResponseDto {

    private Long id;
    private String username;
    private String role;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole().name().substring("ROLE_".length());
    }

}
