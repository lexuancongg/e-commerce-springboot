package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductOptionService;
import com.lexuancong.product.dto.productoptions.ProductOptionGetResponse;
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

    // đã check
    @GetMapping("/management/product-options")
    public ResponseEntity<List<ProductOptionGetResponse>> getProductOptions() {
        return ResponseEntity.ok(this.productOptionService.getProductOptions());

    }

    // đã check
    @PostMapping("/management/product-options")
    public ResponseEntity<ProductOptionGetResponse> createProductOption(
            @Valid @RequestBody ProductOptionCreateRequest ProductOptionCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
    ){
        ProductOptionGetResponse productOptionSaved = this.productOptionService.createProductOption(ProductOptionCreateRequest);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/product-options/{id}")
                        .buildAndExpand(productOptionSaved.id()).toUri())
                .body(productOptionSaved);

    }

    // đã check
    @PutMapping("/management/product-options/{id}")
    public ResponseEntity<Void> updateProductOption(
            @PathVariable(name = "id") Long id ,
            @Valid @RequestBody ProductOptionCreateRequest productOptionCreateRequest
    ){
        this.productOptionService.updateProductOption(id, productOptionCreateRequest);
        return ResponseEntity.ok().build();
    }

    // đã chekc
    @DeleteMapping("/management/product-options/{id}")
    public ResponseEntity<Void> deleteProductOption(
            @PathVariable Long id
    ){
        this.productOptionService.deleteProductOption(id);
        return ResponseEntity.ok().build();
    }


}
