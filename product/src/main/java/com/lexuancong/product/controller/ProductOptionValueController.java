package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductOptionValueService;
import com.lexuancong.product.dto.productoptionvalue.ProductOptionValueGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductOptionValueController {
    private final ProductOptionValueService productOptionValueService;


    // đã check
    @GetMapping("/management/product-option-values")
    public ResponseEntity<List<ProductOptionValueGetResponse>> getProductOptionValues() {
        return ResponseEntity.ok(this.productOptionValueService.getProductOptionValues());

    }


    // đã check
    @GetMapping("/management/product-option-values/{productId}")
    public ResponseEntity<List<ProductOptionValueGetResponse>> getProductOptionValuesByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(this.productOptionValueService.getProductOptionValuesByProductId(productId));
    }




}
