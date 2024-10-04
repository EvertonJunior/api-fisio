package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.CareType;
import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.entities.Patient;
import com.ejunior.fisio_api.exceptions.CpfUniqueViolationException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.CareTypeRepository;
import com.ejunior.fisio_api.repositories.PatientRepository;
import com.ejunior.fisio_api.repositories.projections.PatientProjection;
import com.ejunior.fisio_api.web.dtos.PatientCreateDto;
import com.ejunior.fisio_api.web.dtos.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository repository;

    @Transactional
    public Patient save(Patient patient){
        try{
            return repository.save(patient);
        } catch (DataIntegrityViolationException e){
            throw new CpfUniqueViolationException("CPF ja cadastrado no sistema.");
        }
    }

    @Transactional(readOnly = true)
    public Patient findById(long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " nao encontrado"));
    }


    @Transactional(readOnly = true)
    public Patient findByCpf(String cpf){
        return repository.findByCpf(cpf).orElseThrow(() -> new NotFoundException("cpf " + cpf + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<PatientProjection> findByHospitalId(long id,  Pageable pageable){
        Page<PatientProjection> patients = repository.findByHospitalId(id, pageable);
        if (patients.isEmpty()){
            throw new NotFoundException("Nenhum atendimento encotrado");
        }
        return patients;
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }

}
