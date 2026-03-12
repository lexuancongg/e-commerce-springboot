package com.lexuancong.address.repository;

import com.lexuancong.address.model.Country;
import com.lexuancong.address.model.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province,Long>, JpaSpecificationExecutor<Province> {

    List<Province> findAllByCountryIdOrderByNameAsc(Long countryId);


    boolean existsByNameIgnoreCaseAndCountryIdAndIdNot(String name, Long countryId, Long id);


    boolean existsByCountry_Id(Long countryId);
}
