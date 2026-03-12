package com.lexuancong.address.repository;

import com.lexuancong.address.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {

    // tương đương sql :
    // select exits (select 1 from country where lower(name) = lower(:name) and  id != :id )
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
