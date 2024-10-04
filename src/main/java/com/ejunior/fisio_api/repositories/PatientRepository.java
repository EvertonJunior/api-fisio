package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.Patient;
import com.ejunior.fisio_api.repositories.projections.PatientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByCpf(String cpf);

    Page<PatientProjection> findByHospitalId(long id, Pageable pageable);

    @Query(value = "SELECT c FROM Patient c", nativeQuery = false)
    Page<PatientProjection> findAllPageable(Pageable pageable);
}
