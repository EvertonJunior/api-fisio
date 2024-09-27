package com.ejunior.fisio_api.web.mapper;

import com.ejunior.fisio_api.entities.User;
import com.ejunior.fisio_api.web.dtos.UserCreateDto;
import org.modelmapper.ModelMapper;

public class UserMapper {

    public static User toUserCreateDto(UserCreateDto createDto){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(createDto, User.class);
    }
}
