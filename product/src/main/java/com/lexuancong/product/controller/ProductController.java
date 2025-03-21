package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductService;
import com.lexuancong.product.viewmodel.product.post.ProductPostVm;
import com.lexuancong.product.viewmodel.product.post.ProductSummaryVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // create
    @PostMapping({"/backoffice/products"})
    public ResponseEntity<ProductSummaryVm> createProduct(@Valid @RequestBody ProductPostVm productPostVm) {
        ProductSummaryVm productSummaryVm = this.productService.createProduct(productPostVm);
        return new ResponseEntity<>(productSummaryVm, HttpStatus.CREATED);

    }
    @PutMapping(path = "/backoffice/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProductPostVm productPostVm) {
        this.productService.updateProduct(id,productPostVm);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }





}
