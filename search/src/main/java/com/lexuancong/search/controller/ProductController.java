package com.lexuancong.search.controller;

import com.lexuancong.search.service.SearchProductService;
import com.lexuancong.search.viewmodel.ProductPagingVm;
import com.lexuancong.search.viewmodel.ProductQueryParams;
import com.lexuancong.share.constants.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final SearchProductService searchProductService;

    public ProductController(SearchProductService searchProductService) {
        this.searchProductService = searchProductService;
    }
    @GetMapping("/customer/search")
    public ResponseEntity<ProductPagingVm> searchProduct(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(name = "pageSize" , defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand

    ){
        ProductQueryParams  criteria = new ProductQueryParams(keyword,pageIndex,pageSize,category,minPrice,maxPrice,brand);
        return ResponseEntity.ok(
                this.searchProductService.findProductsByCriteria(criteria)
        );

    }
}
