package com.lexuancong.product.controller;

import com.lexuancong.product.service.SpecificProductVariantService;
import com.lexuancong.product.dto.productoptionvalue.ProductOptionValueGetResponse;
import com.lexuancong.product.dto.specificproductvariant.SpecificProductVariantGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpecificProductVariantController {
    private final SpecificProductVariantService specificProductVariantService;

    // lấy thông tin biến thể cụ thể dựa vào sản phẩm cha
    @GetMapping("/customer/specific-product-variants/{productId}")
    public ResponseEntity<List<SpecificProductVariantGetResponse>> getSpecificProductVariantsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(this.specificProductVariantService.getSpecificProductVariantsByProductId(productId));
    }



    // lấy option cụ thể tương ứng vs mỗi biến tể

    @GetMapping("/customer/specific-product-variants/option-values")
    ResponseEntity< List<ProductOptionValueGetResponse>> getProductOptionValuesOfSpecificProductVariants(
            @RequestParam(value = "productIds") List<Long> productIds

    ) {
        return ResponseEntity.ok(
                this.specificProductVariantService.getProductOptionValuesOfSpecificProductVariants(productIds)
        );

    }



}
