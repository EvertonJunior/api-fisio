package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.exceptions.CnpjUniqueViolantionException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.HospitalRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository repository;

    @Transactional
    public Hospital save(Hospital hospital){
        try{
            return repository.save(hospital);
        }
        catch(DataIntegrityViolationException e){
            throw new CnpjUniqueViolantionException("Cnpj ja cadastrado no sistema");
        }
    }

    @Transactional(readOnly = true)
    public Hospital findById(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id "+ id + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public Hospital findByCnpj(String cnpj){
        return repository.findByCnpj(cnpj).orElseThrow(() -> new NotFoundException("id "+ cnpj + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Hospital> findAll(){
        return repository.findAll();
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }
}
