package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.District;
import com.lexuancong.address.repository.DistrictRepository;
import com.lexuancong.address.repository.ProvinceRepository;
import com.lexuancong.address.viewmodel.district.DistrictGetVm;
import com.lexuancong.share.exception.NotFoundException;
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

    public List<DistrictGetVm> getDistrictByProvinceId(Long provinceId) {
        // check provinceId
        boolean isExitedProvince = provinceRepository.existsById(provinceId);
        if (!isExitedProvince) {
           throw new NotFoundException(Constants.ErrorKey.Province.PROVINCE_NOT_FOUND,provinceId);
        }
        List<District> districts = districtRepository.findAllByProvinceIdOrderByNameAsc(provinceId);
        return districts.stream()
                .map(DistrictGetVm::fromModel)
                .toList();
    }
}
