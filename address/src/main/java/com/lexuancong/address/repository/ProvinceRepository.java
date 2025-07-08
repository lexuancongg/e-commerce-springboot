package com.lexuancong.address.repository;

import com.lexuancong.address.model.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province,Long>, JpaSpecificationExecutor<Province> {
    @Query(value = "select province " +
            "from Province province " +
            "where province.country.id =: countryId or :countryId is null " +
            "order by province.name ASC"
    )
    Page<Province> getProvincesPagingByCountryId(@Param("countryId") Long countryId, Pageable pageable);
    List<Province> findAllByCountryIdOrderByNameAsc(Long countryId);

    boolean existsByNameIgnoreCaseAndCountryId(String name, Long countryId);

    boolean existsByNameIgnoreCaseAndCountryIdAndIdNot(String name, Long countryId, Long id);
}
