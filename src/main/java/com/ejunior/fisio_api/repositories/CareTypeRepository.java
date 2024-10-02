package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.CareType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareTypeRepository extends JpaRepository<CareType, Long> {
}
