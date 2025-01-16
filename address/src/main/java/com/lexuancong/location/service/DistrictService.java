package com.lexuancong.location.service;

import com.lexuancong.location.model.District;
import com.lexuancong.location.repository.DistrictRepository;
import com.lexuancong.location.repository.ProvinceRepository;
import com.lexuancong.location.viewmodel.district.DistrictVm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;

    public DistrictService(DistrictRepository districtRepository, ProvinceRepository provinceRepository) {
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
    }

    public List<DistrictVm> getDistrictByProvinceId(Long provinceId) {
        // check provinceId
        boolean isExistProvinceId = provinceRepository.existsById(provinceId);
        if (!isExistProvinceId) {
            // throw exception
        }
        List<District> districts = districtRepository.findAllByProvinceIdOrderByNameAsc(provinceId);
        return districts.stream()
                .map(DistrictVm::fromModel)
                .toList();
    }
}
