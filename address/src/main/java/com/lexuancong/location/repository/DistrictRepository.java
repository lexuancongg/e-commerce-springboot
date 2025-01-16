package com.lexuancong.location.repository;

import com.lexuancong.location.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District,Long> {
    List<District> findAllByProvinceIdOrderByNameAsc(Long provinceId);
}
