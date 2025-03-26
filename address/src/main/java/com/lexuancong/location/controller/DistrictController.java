package com.lexuancong.location.controller;

import com.lexuancong.location.service.DistrictService;
import com.lexuancong.location.viewmodel.district.DistrictVm;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;


    // lấy district dựa vào province
    @GetMapping({"/management/district/{provinceId}","/customer/district/{provinceId}"})
    public ResponseEntity<List<DistrictVm>>  getDistrictByProvinceId(@PathVariable long provinceId){
        List<DistrictVm> districtVms = districtService.getDistrictByProvinceId(provinceId);
        return ResponseEntity.ok(districtVms);
    }
}
