package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductOptionService;
import com.lexuancong.product.viewmodel.productoptions.ProductOptionGetVm;
import com.lexuancong.product.viewmodel.productoptions.ProductOptionPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<List<ProductOptionGetVm>> getProductOptions() {
        return ResponseEntity.ok(this.productOptionService.getProductOptions());

    }

    // đã check
    @PostMapping("/management/product-options")
    public ResponseEntity<ProductOptionGetVm> createProductOption(
            @Valid @RequestBody ProductOptionPostVm ProductOptionPostVm,
            UriComponentsBuilder uriComponentsBuilder
    ){
        ProductOptionGetVm productOptionSaved = this.productOptionService.createProductOption(ProductOptionPostVm);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/product-options/{id}")
                        .buildAndExpand(productOptionSaved.id()).toUri())
                .body(productOptionSaved);

    }

    // đã check
    @PutMapping("/management/product-options/{id}")
    public ResponseEntity<Void> updateProductOption(
            @PathVariable(name = "id") Long id ,
            @Valid @RequestBody ProductOptionPostVm productOptionPostVm
    ){
        this.productOptionService.updateProductOption(id,productOptionPostVm);
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
