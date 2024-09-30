package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.repositories.projections.PhysicalTherapistProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PhysicalTherapistRepository extends JpaRepository<PhysicalTherapist, Long> {

    PhysicalTherapist findByUserId(long id);

    Optional<PhysicalTherapist> findPhysicalTherapistByCpf(String cpf);

    @Query("select c FROM PhysicalTherapist c")
    Page<PhysicalTherapistProjection> findAllPageable(Pageable pageable);
}
