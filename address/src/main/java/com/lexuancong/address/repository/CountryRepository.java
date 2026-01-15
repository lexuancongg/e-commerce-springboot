package com.lexuancong.address.repository;

import com.lexuancong.address.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
