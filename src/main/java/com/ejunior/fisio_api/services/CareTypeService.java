package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.CareType;
import com.ejunior.fisio_api.exceptions.CodeUniqueViolationException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.CareTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareTypeService {

    private final CareTypeRepository repository;

    @Transactional
    public CareType save(CareType careType){
        try{
            return repository.save(careType);
        }catch (DataIntegrityViolationException e) {
            throw new CodeUniqueViolationException("Codigo ja cadastrado no sistema");
        }
    }

    @Transactional(readOnly = true)
    public CareType findById(long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<CareType> findAll(){
        return repository.findAll();
    }

    @Transactional
    public void updatePriceById(long id, double newPrice){
        CareType careType = findById(id);
        careType.setPrice(newPrice);
        repository.save(careType);
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }
}
