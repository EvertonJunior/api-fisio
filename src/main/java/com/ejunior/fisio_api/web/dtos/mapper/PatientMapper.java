package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.entities.Patient;
import com.ejunior.fisio_api.services.HospitalService;
import com.ejunior.fisio_api.web.dtos.PatientCreateDto;
import com.ejunior.fisio_api.web.dtos.PatientResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientMapper {

    private final HospitalService hospitalService;

    public Patient toPatient(PatientCreateDto dto){
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setCpf(dto.getCpf());
        patient.setHospital(hospitalService.findById(dto.getHospId()));
        return patient;
    }

    public static PatientResponseDto toDto(Patient patient){
        return new ModelMapper().map(patient, PatientResponseDto.class);
    }

}
