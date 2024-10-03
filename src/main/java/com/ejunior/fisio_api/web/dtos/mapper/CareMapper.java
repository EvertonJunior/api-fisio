package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.entities.Care;
import com.ejunior.fisio_api.entities.CareType;
import com.ejunior.fisio_api.services.CareTypeService;
import com.ejunior.fisio_api.services.HospitalService;
import com.ejunior.fisio_api.services.PatientService;
import com.ejunior.fisio_api.web.dtos.CareCreateDto;
import com.ejunior.fisio_api.web.dtos.CareResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class CareMapper {

    private final CareTypeService careTypeService;
    private final HospitalService hospitalService;
    private final PatientService patientService;

    public Care dtoToCare(CareCreateDto createDto){
        Care care = new Care();
        care.setType(careTypeService.findById(createDto.getCareTypeId()));
        care.setName(care.getType().getName());
        care.setPrice(care.getType().getPrice());
        care.setHospital(hospitalService.findById(createDto.getHospitalId()));
        care.setPatient(patientService.findById(createDto.getPatientId()));
        care.setDescription(createDto.getDescription());
        return care;
    }

    public CareResponseDto careToDto (Care care){
        CareResponseDto careResponseDto = new CareResponseDto();
        careResponseDto.setPhysicalTherapistName(care.getPhysicalTherapist().getName());
        careResponseDto.setDate(care.getDate());
        careResponseDto.setHospitalName(care.getHospital().getName());
        careResponseDto.setCareName(care.getType().getName());
        careResponseDto.setPatientName(care.getPatient().getName());
        careResponseDto.setDescription(care.getDescription());
        return careResponseDto;
    }
}

