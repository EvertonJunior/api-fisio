package com.ejunior.fisio_api.web.dtos.mapper;


import com.ejunior.fisio_api.entities.Manager;
import com.ejunior.fisio_api.web.dtos.ManagerCreateDto;
import com.ejunior.fisio_api.web.dtos.ManagerResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManagerMapper {

    public static Manager toManager(ManagerCreateDto createDto){
        return new ModelMapper().map(createDto, Manager.class);
    }

    public static ManagerResponseDto toDto(Manager manager){
        return new ModelMapper().map(manager, ManagerResponseDto.class);
    }
}
