package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.web.dtos.PhysicalTherapistCreateDto;
import com.ejunior.fisio_api.web.dtos.PhysicalTherapistResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhysicalTherapistsMapper {

    public static PhysicalTherapist toPhysicalTherapist(PhysicalTherapistCreateDto physicalTherapistCreateDto ){
        return new ModelMapper().map(physicalTherapistCreateDto, PhysicalTherapist.class);
    }

    public static PhysicalTherapistResponseDto toDto(PhysicalTherapist physicalTherapist ){
        return new ModelMapper().map(physicalTherapist, PhysicalTherapistResponseDto.class);
    }
}
