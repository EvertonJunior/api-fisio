package com.ejunior.fisio_api.repositories.projections;

import com.ejunior.fisio_api.entities.Hospital;

public interface PatientProjection {

    Long getId();
    String getName();
    String getCpf();
    String getHospitalName();
}
