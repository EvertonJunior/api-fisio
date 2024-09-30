package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.exceptions.CpfUniqueViolationException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.PhysicalTherapistRepository;

import com.ejunior.fisio_api.repositories.projections.PhysicalTherapistProjection;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class PhysicalTherapistService {

    private final PhysicalTherapistRepository repository;


    @Transactional
    public PhysicalTherapist save(PhysicalTherapist pT){
        try{
            return repository.save(pT);
        }
        catch (DataIntegrityViolationException e){
            throw new CpfUniqueViolationException("CPF fornecido ja existe no sistema");
        }
    }

    @Transactional(readOnly = true)
    public PhysicalTherapist findById(long id){
        return repository.findById(id).orElseThrow(()-> new NotFoundException("id "+ id + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public PhysicalTherapist findByCpf(String cpf){
        return repository.findPhysicalTherapistByCpf(cpf).orElseThrow(()-> new NotFoundException("id "+ cpf + " nao encontrado"));
    }


   @Transactional(readOnly = true)
    public Page<PhysicalTherapistProjection> findAll(Pageable pageable){
        return repository.findAllPageable(pageable);
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PhysicalTherapist findByUserId(long id) {
        return repository.findByUserId(id);
    }
}
