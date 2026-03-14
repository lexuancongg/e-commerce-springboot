package com.lexuancong.product.controller;

import com.lexuancong.product.service.BrandService;
import com.lexuancong.product.dto.brand.BrandCreateRequest;
import com.lexuancong.product.dto.brand.BrandResponse;
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



    @GetMapping({"/management/brands","/customer/brands"})
    public ResponseEntity<List<BrandResponse>> getBrands(
            @RequestParam(name = "brandName" , required = false,defaultValue = "") String brandName
            ) {
        List<BrandResponse> brands = brandService.getBrands(brandName);
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }



    @PostMapping("/management/brands")
    public ResponseEntity<BrandResponse> createBrand(
            @RequestBody @Valid BrandCreateRequest brandCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
            ){
        BrandResponse brandSaved  = brandService.createBrand(brandCreateRequest);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/brands/{id}")
                        .buildAndExpand(brandSaved.id()).toUri())
                .body(brandSaved);

    }

    @PutMapping("/management/brands/{id}")
    public ResponseEntity<Void> updateBrand(@PathVariable Long id, @RequestBody @Valid BrandCreateRequest brandCreateRequest) {
        brandService.updateBrand(id, brandCreateRequest);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/management/brands/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }


}
