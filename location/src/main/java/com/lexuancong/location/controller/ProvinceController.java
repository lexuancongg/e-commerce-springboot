package com.lexuancong.location.controller;

import com.lexuancong.location.service.ProvinceService;
import com.lexuancong.location.utils.Constants;
import com.lexuancong.location.viewmodel.province.ProvincePagingVm;
import com.lexuancong.location.viewmodel.province.ProvincePostVm;
import com.lexuancong.location.viewmodel.province.ProvinceVm;
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

    @GetMapping("/backoffice/provinces/paging")
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
    // show province with country
    @GetMapping({"/backoffice/provinces/{countryId}","/storefront/provinces/{countryId}"})
    public ResponseEntity<List<ProvinceVm>> getProvincesByCountryId(
            @PathVariable(value = "countryId",required = true) final Long countryId
    ) {
        List<ProvinceVm> provinceVms = provinceService.getProvincesByCountryId(countryId);
        return ResponseEntity.ok(provinceVms);
    }

    @PostMapping("/backoffice/provinces")
    public ResponseEntity<ProvinceVm> createProvince(
            @RequestBody @Valid final ProvincePostVm provincePostVm,
            UriComponentsBuilder uriComponentsBuilder
            ){
        ProvinceVm provinceSaved = provinceService.createProvince(provincePostVm);
        return ResponseEntity.created(
                uriComponentsBuilder.replacePath("/provinces/{id}")
                        .buildAndExpand(provinceSaved.id()).toUri()
        ).body(provinceSaved);
    }

    @PutMapping("/backoffice/provinces/{id}")
    public ResponseEntity<Void> updateProvince(@PathVariable Long id ,
                                               @RequestBody @Valid ProvincePostVm provincePostVm){
        provinceService.updateProvince(id,provincePostVm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/backoffice/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id){
        provinceService.deleteProvince(id);
        return ResponseEntity.noContent().build();
    }

}
