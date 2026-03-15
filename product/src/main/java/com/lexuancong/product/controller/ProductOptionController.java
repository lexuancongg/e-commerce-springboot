package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductOptionService;
import com.lexuancong.product.dto.productoptions.ProductOptionResponse;
import com.lexuancong.product.dto.productoptions.ProductOptionCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    @GetMapping("/management/product-options")
    public ResponseEntity<List<ProductOptionResponse>> getProductOptions() {
        return ResponseEntity.ok(this.productOptionService.getProductOptions());

    }

    @PostMapping("/management/product-options")
    public ResponseEntity<ProductOptionResponse> createProductOption(
            @Valid @RequestBody ProductOptionCreateRequest ProductOptionCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
    ){
        ProductOptionResponse productOptionSaved = this.productOptionService.createProductOption(ProductOptionCreateRequest);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("management/product-options/{id}")
                        .buildAndExpand(productOptionSaved.id()).toUri())
                .body(productOptionSaved);

    }

    @PutMapping("/management/product-options/{id}")
    public ResponseEntity<Void> updateProductOption(
            @PathVariable(name = "id") Long id ,
            @Valid @RequestBody ProductOptionCreateRequest productOptionCreateRequest
    ){
        this.productOptionService.updateProductOption(id, productOptionCreateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/management/product-options/{id}")
    public ResponseEntity<Void> deleteProductOption(
            @PathVariable Long id
    ){
        this.productOptionService.deleteProductOption(id);
        return ResponseEntity.ok().build();
    }


}
