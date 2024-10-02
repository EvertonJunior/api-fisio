package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.web.dtos.HospitalCreateDto;
import com.ejunior.fisio_api.web.dtos.HospitalResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HospitalMapper {

    public static Hospital toHospital(HospitalCreateDto dto){
        return new ModelMapper().map(dto, Hospital.class);
    }

    public static HospitalResponseDto toDto(Hospital hospital){
        return new ModelMapper().map(hospital, HospitalResponseDto.class);
    }

}
