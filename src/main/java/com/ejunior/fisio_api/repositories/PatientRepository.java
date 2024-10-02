package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
