package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.Care;
import com.ejunior.fisio_api.repositories.projections.CareProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CareRepository extends JpaRepository<Care, Long> {

    List<Care> findByPatientId(long id);

    List<Care> findByPhysicalTherapistId(long id);

    Page<CareProjection> findByHospitalId(long id, Pageable pageable);

    Page<CareProjection> findByPhysicalTherapistUserId(long userId, Pageable pageable);
}
