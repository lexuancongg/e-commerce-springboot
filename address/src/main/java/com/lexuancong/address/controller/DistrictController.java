package com.lexuancong.address.controller;

import com.lexuancong.address.service.DistrictService;
import com.lexuancong.address.viewmodel.district.DistrictGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;


    // lấy district dựa vào province => checked
    @GetMapping({"/management/districts/{provinceId}","/customer/districts/{provinceId}"})
    public ResponseEntity<List<DistrictGetVm>>  getDistrictByProvinceId(@PathVariable long provinceId){
        List<DistrictGetVm> districtGetVms = districtService.getDistrictByProvinceId(provinceId);
        return ResponseEntity.ok(districtGetVms);
    }
}
