package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductAttributeValueService;
import com.lexuancong.product.viewmodel.productattribute.ProductAttributeValuePostVm;
import com.lexuancong.product.viewmodel.productattribute.ProductAttributeValueVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
public class ProductAttributeValueController {
    private final ProductAttributeValueService productAttributeValueService;
    public ProductAttributeValueController(ProductAttributeValueService productAttributeValueService) {
        this.productAttributeValueService = productAttributeValueService;
    }


    // đã check
    @GetMapping("/management/product-attribute-values/{productId}")
    public ResponseEntity<List<ProductAttributeValueVm>> getProductAttributeValueByProductId (
            @PathVariable("productId") Long productId) {
        List<ProductAttributeValueVm> productAttributeValueVms = this.productAttributeValueService
                .getProductAttributeValueByProductId(productId);
        return new ResponseEntity<>(productAttributeValueVms, HttpStatus.OK);

    }


    // đã check
    @PostMapping("/management/product-attribute-values")
    public ResponseEntity<ProductAttributeValueVm> createProductAttributeValue(
            @Valid @RequestBody ProductAttributeValuePostVm productAttributeValuePostVm,
            UriComponentsBuilder uriComponentsBuilder
            ) {
        ProductAttributeValueVm productAttributeValueVm = this.productAttributeValueService
                .createProductAttributeValue(productAttributeValuePostVm);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/backoffice/product-attribute-value/{id}")
                .buildAndExpand(productAttributeValueVm.id()).toUri())
                .body(productAttributeValueVm);
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
                                                            @Valid @RequestBody ProductAttributeValuePostVm productAttributeValuePostVm){
        this.productAttributeValueService.updateProductAttributeValue(id,productAttributeValuePostVm);
        return ResponseEntity.noContent().build();

    }



}
