package com.lexuancong.address.controller;

import com.lexuancong.address.service.ProvinceService;
import com.lexuancong.address.viewmodel.province.ProvincePagingVm;
import com.lexuancong.address.viewmodel.province.ProvincePostVm;
import com.lexuancong.address.viewmodel.province.ProvinceGetVm;
import com.lexuancong.share.constants.Constants;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProvincePagingVm> getProvincesPaging(
            @RequestParam(value = "pageIndex",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false)
            final  int pageSize,
            @RequestParam(value = "countryId",required = false) final Long countryId
    ) {
        ProvincePagingVm provincePagingVm = provinceService.getProvincesPaging(pageIndex,pageSize,countryId);
        return ResponseEntity.ok(provincePagingVm);

    }



    // checked
    @GetMapping({"/management/provinces/{countryId}","/customer/provinces/{countryId}"})
    public ResponseEntity<List<ProvinceGetVm>> getProvincesByCountryId(
            @PathVariable(value = "countryId") final Long countryId
    ) {
        List<ProvinceGetVm> provinceGetVms = provinceService.getProvincesByCountryId(countryId);
        return ResponseEntity.ok(provinceGetVms);
    }

    // checked

    @PostMapping("/management/provinces")
    public ResponseEntity<ProvinceGetVm> createProvince(
            @RequestBody @Valid final ProvincePostVm provincePostVm,
            UriComponentsBuilder uriComponentsBuilder
            ){
        ProvinceGetVm provinceSaved = provinceService.createProvince(provincePostVm);
        return ResponseEntity.created(
                uriComponentsBuilder.replacePath("/provinces/{id}")
                        .buildAndExpand(provinceSaved.id()).toUri()
        ).body(provinceSaved);
    }



    @PutMapping("/management/provinces/{id}")
    public ResponseEntity<Void> updateProvince(@PathVariable Long id ,
                                               @RequestBody @Valid ProvincePostVm provincePostVm){
        provinceService.updateProvince(id,provincePostVm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/management/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id){
        provinceService.deleteProvince(id);
        return ResponseEntity.noContent().build();
    }

}
