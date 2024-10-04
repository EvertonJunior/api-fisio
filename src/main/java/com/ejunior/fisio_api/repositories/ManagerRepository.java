package com.ejunior.fisio_api.repositories;

import com.ejunior.fisio_api.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

   Optional<Manager> findByCpf(String cpf);

    Manager findByUserId(long id);
}
