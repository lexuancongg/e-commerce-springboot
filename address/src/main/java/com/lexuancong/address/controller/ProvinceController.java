package com.lexuancong.address.controller;

import com.lexuancong.address.service.ProvinceService;
import com.lexuancong.address.dto.province.ProvinceCreateRequest;
import com.lexuancong.address.dto.province.ProvinceResponse;
import com.lexuancong.share.constants.Constants;
import com.lexuancong.share.dto.paging.PagingResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@RestController
public class ProvinceController {
    private final ProvinceService provinceService;
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }


    // checked
    @GetMapping("/management/provinces/paging")
    public ResponseEntity<PagingResponse<ProvinceResponse>> getProvincesPaging(
            @RequestParam(value = "pageIndex",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false)
            final  int pageSize,
            @RequestParam(value = "countryId",required = false) final Long countryId
    ) {
        PagingResponse<ProvinceResponse> provincePaging = provinceService.getProvincesPaging(pageIndex,pageSize,countryId);
        return ResponseEntity.ok(provincePaging);

    }



    // checked
    @GetMapping({"/management/provinces/{countryId}","/customer/provinces/{countryId}"})
    public ResponseEntity<List<ProvinceResponse>> getProvincesByCountryId(
            @PathVariable(value = "countryId") final Long countryId
    ) {
        List<ProvinceResponse> provinces = provinceService.getProvincesByCountryId(countryId);
        return ResponseEntity.ok(provinces);
    }



    @PostMapping("/management/provinces")
    public ResponseEntity<ProvinceResponse> createProvince(
            @RequestBody @Valid final ProvinceCreateRequest provinceCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
            ){
        ProvinceResponse provinceSaved = provinceService.createProvince(provinceCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(provinceSaved);
    }



    @PutMapping("/management/provinces/{id}")
    public ResponseEntity<Void> updateProvince(@PathVariable Long id ,
                                               @RequestBody @Valid ProvinceCreateRequest provinceCreateRequest){
        provinceService.updateProvince(id, provinceCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/management/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id){
        provinceService.deleteProvince(id);
        return ResponseEntity.noContent().build();
    }

}
