package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    // todo Evitar duplicatas de serviços pelo nome (ignorando maiúsculas/minúsculas)
    Optional<Services> findByNameIgnoreCase(String name);

    // todo Se você usa tipo (por exemplo, STREAMING, LANGUAGE, etc.)
    List<Services> findByTypeIgnoreCase(String type);

    // todo Caso queira filtrar por país/região no futuro
    // List<Services> findByAllowedCountryCode(String countryCode);}
}