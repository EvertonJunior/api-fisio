package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.CareType;
import com.ejunior.fisio_api.entities.Patient;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.CareTypeRepository;
import com.ejunior.fisio_api.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository repository;

    @Transactional
    public Patient findById(long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " nao encontrado"));
    }
}
