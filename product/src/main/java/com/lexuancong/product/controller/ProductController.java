package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductService;
import com.lexuancong.product.utils.Constants;
import com.lexuancong.product.viewmodel.product.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/api/product")
@RestController
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // create
    @PostMapping({"/management/products"})
    public ResponseEntity<ProductSummaryVm> createProduct(@Valid @RequestBody ProductPostVm productPostVm) {
        ProductSummaryVm productSummaryVm = this.productService.createProduct(productPostVm);
        return new ResponseEntity<>(productSummaryVm, HttpStatus.CREATED);

    }
    @PutMapping(path = "/management/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProductPostVm productPostVm) {
        this.productService.updateProduct(id,productPostVm);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    // lấy danh sách nổi bật hiển thị đầu tiên
    @GetMapping("/customer/products/featured")
    public ResponseEntity<ProductFeaturePagingVm> getFeaturedProductsPaging(
            @RequestParam(value = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        return ResponseEntity.ok(productService.getFeaturedProductsPaging(pageIndex,pageSize));
    }


    // xem chi tiết sp
    @GetMapping("/customer/products/{slug}")
    public ResponseEntity<ProductDetailVm> getProductDetail(@PathVariable("slug") String slug) {
        return ResponseEntity.ok(this.productService.getProductDetail(slug));

    }

    // xóa sp
    @DeleteMapping("/management/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        this.productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





    // lây ra ds sp
    @GetMapping({"/customer/products"})
    public ResponseEntity<ProductPagingVm> getProductsPaging(
            @RequestParam(value = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE ,required = false) int pageSize
    ){
        return ResponseEntity.ok(this.productService.getProductsPaging(pageIndex,pageSize));

    }



    // lấy ra ds sp trong category
    @GetMapping({"/customer/category/{categoryId}/products"})
    public ResponseEntity<ProductPagingVm> getProductsFromCategoryPaging(
            @RequestParam(value = "pageIndex",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @PathVariable Long categoryId
    ){
        return ResponseEntity.ok(this.productService.getProductsFromCategoryPaging(pageIndex,pageSize,categoryId));
    }


    // api lấy ngẫu nhiên ds sp nôổi bật lam slide
    @GetMapping({"/customer/products/featured/slide"})
    public ResponseEntity<List<ProductPreviewVm>> getProductFeaturedMakeSlide(){
        List<ProductPreviewVm> productPreviewVms =  productService.getProductFeaturedMakeSlide();
        return new ResponseEntity<>(productPreviewVms, HttpStatus.OK);
    }

    @GetMapping("/customer/products")
    public ResponseEntity<List<ProductPreviewVm>> getProductsByIds (
            @RequestParam("productIds") List<Long> productIds
    ){
        return ResponseEntity.ok(this.productService.getProductsByIds(productIds));
    }





}
