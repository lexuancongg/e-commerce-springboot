package com.lexuancong.product.controller;

import com.lexuancong.product.service.SpecificProductVariantService;
import com.lexuancong.product.viewmodel.specificproductvariant.SpecificProductVariantGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpecificProductVariantController {
    private final SpecificProductVariantService specificProductVariantService;

    // lấy danh sách để hiển thị options => đã check
    @GetMapping("/customer/specific-product-variants/{productId}")
    public ResponseEntity<List<SpecificProductVariantGetVm>> getSpecificProductVariantsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(this.specificProductVariantService.getSpecificProductVariantsByProductId(productId));
    }



}
