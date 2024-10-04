package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.Manager;
import com.ejunior.fisio_api.exceptions.CpfUniqueViolationException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.jwt.JwtUserDetails;
import com.ejunior.fisio_api.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository repository;

    @Transactional
    public Manager save(Manager manager){
        try{
            return repository.save(manager);
        } catch(DataIntegrityViolationException e){
            throw new CpfUniqueViolationException("CPF ja cadastrado no sistema");
        }
    }

    @Transactional(readOnly = true)
    public Manager findById(long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public Manager findByCpf(String cpf){
        return repository.findByCpf(cpf).orElseThrow(() -> new NotFoundException("cpf " + cpf + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Manager> findAll(){
        return repository.findAll();
    }

    @Transactional
    public void updateSalary(long id, Double newSalary){
        Manager manager = findById(id);
        manager.setSalary(newSalary);
        repository.save(manager);
    }

    @Transactional
    public void updatePosition(long id, String position){
        Manager manager = findById(id);
        manager.setPosition(position);
        repository.save(manager);
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Manager getDetails(long id) {
        return repository.findByUserId(id);
    }
}
