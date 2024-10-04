package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.entities.CareType;
import com.ejunior.fisio_api.web.dtos.CareTypeCreateDto;
import com.ejunior.fisio_api.web.dtos.CareTypeResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CareTypeMapper {

    public static CareType toCareType(CareTypeCreateDto dto){
        return new ModelMapper().map(dto, CareType.class);
    }

    public static CareTypeResponseDto toDto(CareType careType){
        return new ModelMapper().map(careType, CareTypeResponseDto.class);
    }

}
