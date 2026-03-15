package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductAttributeValueService;
import com.lexuancong.product.dto.productattribute.ProductAttributeValueCreateRequest;
import com.lexuancong.product.dto.productattribute.ProductAttributeValueResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ProductAttributeValueController {
    private final ProductAttributeValueService productAttributeValueService;
    public ProductAttributeValueController(ProductAttributeValueService productAttributeValueService) {
        this.productAttributeValueService = productAttributeValueService;
    }


    @GetMapping("/management/product-attribute-values/{productId}")
    public ResponseEntity<List<ProductAttributeValueResponse>> getProductAttributeValueByProductId (
            @PathVariable("productId") Long productId) {
        List<ProductAttributeValueResponse> productAttributeValues = this.productAttributeValueService
                .getProductAttributeValueByProductId(productId);
        return new ResponseEntity<>(productAttributeValues, HttpStatus.OK);

    }


    @PostMapping("/management/product-attribute-values")
    public ResponseEntity<ProductAttributeValueResponse> createProductAttributeValue(
            @Valid @RequestBody ProductAttributeValueCreateRequest productAttributeValueCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
            ) {
        ProductAttributeValueResponse productAttributeValue = this.productAttributeValueService
                .createProductAttributeValue(productAttributeValueCreateRequest);
        return ResponseEntity.ok().body(productAttributeValue);
    }



    @DeleteMapping({"/management/product-attribute-values/{id}"})
    public ResponseEntity<Void> deleteProductAttributeValue(@PathVariable(name = "id") Long id){
        this.productAttributeValueService.deleteProductAttributeValue(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping({"/management/product-attribute-values/{id}"})
    public ResponseEntity<Void> updateProductAttributeValue(@PathVariable(value = "id") Long id,
                                                            @Valid @RequestBody ProductAttributeValueCreateRequest productAttributeValueCreateRequest){
        this.productAttributeValueService.updateProductAttributeValue(id, productAttributeValueCreateRequest);
        return ResponseEntity.noContent().build();

    }



}
