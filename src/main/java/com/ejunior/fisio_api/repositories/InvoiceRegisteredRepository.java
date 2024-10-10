package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.InvoiceRegistered;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InvoiceRegisteredRepository extends JpaRepository<InvoiceRegistered, Long> {

    InvoiceRegistered findByInvoiceId(Long id);
}
