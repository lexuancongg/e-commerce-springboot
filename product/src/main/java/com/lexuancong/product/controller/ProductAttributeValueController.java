package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductAttributeValueService;
import com.lexuancong.product.dto.productattribute.ProductAttributeValueCreateRequest;
import com.lexuancong.product.dto.productattribute.ProductAttributeValueGetResponse;
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


    // đã check
    @GetMapping("/management/product-attribute-values/{productId}")
    public ResponseEntity<List<ProductAttributeValueGetResponse>> getProductAttributeValueByProductId (
            @PathVariable("productId") Long productId) {
        List<ProductAttributeValueGetResponse> productAttributeValueGetResponses = this.productAttributeValueService
                .getProductAttributeValueByProductId(productId);
        return new ResponseEntity<>(productAttributeValueGetResponses, HttpStatus.OK);

    }


    // đã check
    @PostMapping("/management/product-attribute-values")
    public ResponseEntity<ProductAttributeValueGetResponse> createProductAttributeValue(
            @Valid @RequestBody ProductAttributeValueCreateRequest productAttributeValueCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
            ) {
        ProductAttributeValueGetResponse productAttributeValueGetResponse = this.productAttributeValueService
                .createProductAttributeValue(productAttributeValueCreateRequest);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/backoffice/product-attribute-value/{id}")
                .buildAndExpand(productAttributeValueGetResponse.id()).toUri())
                .body(productAttributeValueGetResponse);
    }


    // đã check

    @DeleteMapping({"/management/product-attribute-values/{id}"})
    public ResponseEntity<Void> deleteProductAttributeValue(@PathVariable(name = "id") Long id){
        this.productAttributeValueService.deleteProductAttributeValue(id);
        return ResponseEntity.noContent().build();
    }


    // đã check
    @PutMapping({"/management/product-attribute-values/{id}"})
    public ResponseEntity<Void> updateProductAttributeValue(@PathVariable(value = "id") Long id,
                                                            @Valid @RequestBody ProductAttributeValueCreateRequest productAttributeValueCreateRequest){
        this.productAttributeValueService.updateProductAttributeValue(id, productAttributeValueCreateRequest);
        return ResponseEntity.noContent().build();

    }



}
