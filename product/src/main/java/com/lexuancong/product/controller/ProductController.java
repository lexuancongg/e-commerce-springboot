package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductService;
import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.viewmodel.product.*;
import com.lexuancong.product.viewmodel.product.variants.ProductVariantVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    // create => đã check
    @PostMapping({"/management/products"})
    public ResponseEntity<ProductSummaryVm> createProduct(@Valid @RequestBody ProductPostVm productPostVm) {
        ProductSummaryVm productSummaryVm = this.productService.createProduct(productPostVm);
        return new ResponseEntity<>(productSummaryVm, HttpStatus.CREATED);

    }

    // update // đã check
    @PutMapping(path = "/management/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProductPostVm productPostVm) {
        this.productService.updateProduct(id,productPostVm);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    // ds nổi bật => đã check
    @GetMapping("/customer/products/featured")
    public ResponseEntity<ProductFeaturePagingVm> getFeaturedProductsPaging(
            @RequestParam(value = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        return ResponseEntity.ok(productService.getFeaturedProductsPaging(pageIndex,pageSize));
    }


    // xem chi tiết sp => đã check
    @GetMapping("/customer/products/{slug}")
    public ResponseEntity<ProductDetailVm> getProductDetail(@PathVariable("slug") String slug) {
        return ResponseEntity.ok(this.productService.getProductDetailBySlug(slug));

    }

    // xóa sp => đã check
    @DeleteMapping("/management/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        this.productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





//    // lây ra ds sp
//    @GetMapping({"/customer/products"})
//    public ResponseEntity<ProductPagingVm> getProductsPaging(
//            @RequestParam(value = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
//            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE ,required = false) int pageSize
//    ){
//        return ResponseEntity.ok(this.productService.getProductsPaging(pageIndex,pageSize));
//
//    }



    // lấy ra ds sp trong category dựa vào slug => đã check
    @GetMapping({"/customer/category/{categorySlug}/products"})
    public ResponseEntity<ProductPagingVm> getProductsFromCategoryPaging(
            @RequestParam(value = "pageIndex",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @PathVariable String categorySlug
    ){
        return ResponseEntity.ok(this.productService.getProductsByCategorySlug(pageIndex,pageSize,categorySlug));
    }


    //  lấy ngaaux nhiên sp làm slide => đã check
    @GetMapping({"/customer/products/featured/slide"})
    public ResponseEntity<List<ProductPreviewVm>> getProductFeaturedMakeSlide(){
        List<ProductPreviewVm> productPreviewVms =  productService.getProductFeaturedMakeSlide();
        return new ResponseEntity<>(productPreviewVms, HttpStatus.OK);
    }


    // đã check
    @GetMapping("/customer/products")
    public ResponseEntity<List<ProductPreviewVm>> getProductsByIds (
            @RequestParam("productIds") List<Long> productIds
    ){
        return ResponseEntity.ok(this.productService.getProductsByIds(productIds));
    }



    //
    @GetMapping("/customer/products")
    public ResponseEntity<ProductPagingVm> getProductByMultiParams(
            @RequestParam(value = "pageIndex" ,required = false , defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "productName", defaultValue = "", required = false) String productName,
            @RequestParam(value = "categorySlug", defaultValue = "", required = false) String categorySlug,
            @RequestParam(value = "startPrice", defaultValue = "", required = false) Double startPrice,
            @RequestParam(value = "endPrice", defaultValue = "", required = false) Double endPrice
    ){
        return ResponseEntity.ok(
                this.productService.getProductByMultiParams(pageIndex,pageSize,productName,categorySlug,startPrice,endPrice)
                );

    }




    // lay ds bien the => đã check
    @GetMapping("/customer/product-variations/{parentId}")
    public ResponseEntity<List<ProductVariantVm>> getProductVariationsByParentId(@PathVariable Long parentId) {
        return new ResponseEntity<>(this.productService.getProductVariationsByParentId(parentId),HttpStatus.OK);
    }





}
