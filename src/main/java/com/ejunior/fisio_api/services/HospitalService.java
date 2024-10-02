package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.CareType;
import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.CareTypeRepository;
import com.ejunior.fisio_api.repositories.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository repository;

    @Transactional
    public Hospital findById(long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " nao encontrado"));
    }
}
