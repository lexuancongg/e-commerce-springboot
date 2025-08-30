package com.lexuancong.product.controller;

import com.lexuancong.product.service.BrandService;
import com.lexuancong.product.viewmodel.brand.BrandPostVm;
import com.lexuancong.product.viewmodel.brand.BrandVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class BrandController {
    private final BrandService brandService;
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }



    // đã check
    @GetMapping({"/management/brands","/customer/brands"})
    public ResponseEntity<List<BrandVm>> getBrands(
            @RequestParam(name = "brandName" , required = false,defaultValue = "") String brandName
            ) {
        List<BrandVm> brandVms = brandService.getBrands(brandName);
        return new ResponseEntity<>(brandVms, HttpStatus.OK);
    }


    // đã check

    @PostMapping("/management/brands")
    public ResponseEntity<BrandVm> createBrand(
            @RequestBody @Valid BrandPostVm brandPostVm,
            UriComponentsBuilder uriComponentsBuilder
            ){
        BrandVm brandSaved  = brandService.createBrand(brandPostVm);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/brands/{id}")
                        .buildAndExpand(brandSaved.id()).toUri())
                .body(brandSaved);

    }

    // đã check
    @PutMapping("/management/brands/{id}")
    public ResponseEntity<Void> updateBrand(@PathVariable Long id, @RequestBody @Valid BrandPostVm brandPostVm) {
        brandService.updateBrand(id,brandPostVm);
        return ResponseEntity.noContent().build();
    }


    // đã check
    @DeleteMapping("/management/brands/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }


}
