package com.lexuancong.location.repository;

import com.lexuancong.location.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {
}
