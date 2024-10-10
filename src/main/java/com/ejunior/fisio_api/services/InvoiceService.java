package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.Invoice;
import com.ejunior.fisio_api.exceptions.CodeUniqueViolationException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository repository;

    @Transactional
    public Invoice save(Invoice invoice){
        try {
            return repository.save(invoice);
        } catch (DataIntegrityViolationException e){
            throw new CodeUniqueViolationException("Codigo de fatura " + invoice.getCode() + " ja existe no sistema");
        }
    }

    @Transactional(readOnly = true)
    public Invoice findById(long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Invoice> findAll(){
        return repository.findAll();
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findByHospitalId(long hospitalId) {
        List<Invoice> invoices = repository.findByHospitalId(hospitalId);
        if(invoices.isEmpty()){
            throw new NotFoundException("id " + hospitalId + " nao encontrado");
        }
        return invoices;
    }

}
