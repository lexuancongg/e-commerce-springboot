package com.lexuancong.search.controller;

import com.lexuancong.search.service.ProductService;
import com.lexuancong.search.viewmodel.ProductPagingVm;
import com.lexuancong.search.viewmodel.ProductQueryParams;
import com.lexuancong.share.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// tìm kiếm sp
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/customer/search")
    public ResponseEntity<ProductPagingVm> searchProduct(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(name = "pageSize" , defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String category

    ){
        ProductQueryParams  criteria = new ProductQueryParams(keyword,pageIndex,pageSize,category,minPrice,maxPrice);
        return ResponseEntity.ok(
                this.productService.findProductsByCriteria(criteria)
        );

    }
}
