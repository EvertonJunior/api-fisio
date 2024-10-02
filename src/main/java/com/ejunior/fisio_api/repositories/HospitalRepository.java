package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByCnpj(String cnpj);
}
