package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductService;
import com.lexuancong.product.viewmodel.product.post.ProductPostVm;
import com.lexuancong.product.viewmodel.product.post.ProductSummaryVm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    }


}
