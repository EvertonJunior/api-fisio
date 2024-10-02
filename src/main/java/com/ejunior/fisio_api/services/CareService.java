package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.Care;
import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.jwt.JwtUserDetails;
import com.ejunior.fisio_api.repositories.CareRepository;
import com.ejunior.fisio_api.repositories.CareTypeRepository;
import com.ejunior.fisio_api.repositories.HospitalRepository;
import com.ejunior.fisio_api.repositories.PatientRepository;
import com.ejunior.fisio_api.repositories.projections.CareProjection;
import com.ejunior.fisio_api.web.dtos.CareCreateDto;
import com.ejunior.fisio_api.web.dtos.CareResponseDto;
import com.ejunior.fisio_api.web.dtos.mapper.CareMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareService {

    private final CareRepository repository;
    private final PhysicalTherapistService physicalTherapistService;
    private final CareMapper mapper;

   @Transactional
    public Care create(CareCreateDto careDto, JwtUserDetails userDetails){
       Care care = mapper.dtoToCare(careDto);
       PhysicalTherapist physicalTherapist = physicalTherapistService.findByUserId(userDetails.getId());
       physicalTherapist.setPayment(physicalTherapist.getPayment() + physicalTherapistPayment(care.getPrice()));
       care.setPhysicalTherapist(physicalTherapist);
       physicalTherapistService.save(physicalTherapist);
       return repository.save(care);
    }

    public Double physicalTherapistPayment(Double value){
       return value * 0.10;
    }

    @Transactional(readOnly = true)
    public Care findById(long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Care> findByPatientId(long id){
       List<Care> cares = repository.findByPatientId(id);
       if(cares.isEmpty()){
           throw new NotFoundException("Nenhuma atendimento encotrado para o id: "+ id);
       }
       return cares;
    }

    @Transactional(readOnly = true)
    public List<Care> findByPhysicalTherapistId(long id){
        List<Care> cares = repository.findByPhysicalTherapistId(id);
        if(cares.isEmpty()){
            throw new NotFoundException("Nenhuma atendimento encotrado para o id: " + id);
        }
        return cares;
    }


    @Transactional(readOnly = true)
    public Page<CareProjection> findByHospitalId(long id, Pageable pageable){
        Page<CareProjection> cares = repository.findByHospitalId(id, pageable);
        if(cares.isEmpty()){
            throw new NotFoundException("Nenhuma atendimento encotrado para o id: " + id);
        }
       return cares;
    }

    @Transactional(readOnly = true)
    public Page<CareProjection> findByUserId(long id, Pageable pageable) {
        Page<CareProjection> cares = repository.findByPhysicalTherapistUserId(id, pageable);
        if (cares.isEmpty()) {
            throw new NotFoundException("Nenhum atendimento realizado");
        }
        return cares;
    }

    @Transactional
    public void deleteById(long id){
       Care care = findById(id);
       PhysicalTherapist physicalTherapist = care.getPhysicalTherapist();
       physicalTherapist.setPayment(physicalTherapist.getPayment() - physicalTherapistPayment(care.getPrice()));
       physicalTherapistService.save(physicalTherapist);
       repository.deleteById(id);
    }

}
