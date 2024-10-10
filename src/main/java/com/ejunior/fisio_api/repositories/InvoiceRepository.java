package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByHospitalId(long id);
}
