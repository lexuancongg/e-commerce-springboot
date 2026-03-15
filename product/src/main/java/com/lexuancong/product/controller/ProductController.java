package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductService;
import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.dto.product.*;
import com.lexuancong.product.dto.product.producforwarehouse.ProductInfoGetResponse;
import com.lexuancong.product.dto.product.variants.ProductVariantResponse;
import com.lexuancong.share.dto.paging.PagingResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping({"/management/products"})
    public ResponseEntity<ProductSummaryResponse> createProduct(@Valid @RequestBody ProductParentCreateRequest productCreateRequest) {
        ProductSummaryResponse productSummaryResponse = this.productService.createProduct(productCreateRequest);
        return new ResponseEntity<>(productSummaryResponse, HttpStatus.CREATED);

    }



    @PutMapping(path = "/management/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProductParentCreateRequest productCreateRequest) {
        this.productService.updateProduct(id, productCreateRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/customer/products/featured")
    public ResponseEntity<PagingResponse<ProductPreviewResponse>> getFeaturedProductsPaging(
            @RequestParam(value = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        return ResponseEntity.ok(productService.getFeaturedProductsPaging(pageIndex,pageSize));
    }


    @GetMapping("/customer/products/{slug}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable("slug") String slug) {
        return ResponseEntity.ok(this.productService.getProductDetailBySlug(slug));

    }

    @DeleteMapping("/management/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        this.productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }






    @GetMapping({"/customer/category/{categorySlug}/products"})
    public ResponseEntity<PagingResponse<ProductPreviewResponse>> getProductsByCategoryPaging(
            @RequestParam(value = "pageIndex",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @PathVariable String categorySlug
    ){
        return ResponseEntity.ok(
                this.productService.getProductsByCategorySlug(pageIndex,pageSize,categorySlug));
    }



    @GetMapping({"/customer/products","/internal-order/products"})
    public ResponseEntity<List<ProductPreviewResponse>> getProductsByIds (
            @RequestParam("productIds") List<Long> productIds
    ){
        return ResponseEntity.ok(this.productService.getProductsByIds(productIds));
    }




//    @GetMapping("/customer/products")
//    public ResponseEntity<ProductPagingGetResponse> getProductByMultiParams(
//            @RequestParam(value = "pageIndex" ,required = false , defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageIndex,
//            @RequestParam(value = "pageSize", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
//            @RequestParam(value = "productName", defaultValue = "", required = false) String productName,
//            @RequestParam(value = "categorySlug", defaultValue = "", required = false) String categorySlug,
//            @RequestParam(value = "startPrice", defaultValue = "", required = false) Double startPrice,
//            @RequestParam(value = "endPrice", defaultValue = "", required = false) Double endPrice
//    ){
//        return ResponseEntity.ok(
//                this.productService.getProductByMultiParams(pageIndex,pageSize,productName,categorySlug,startPrice,endPrice)
//                );
//
//    }




    @GetMapping("/customer/product-variations/{parentId}")
    public ResponseEntity<List<ProductVariantResponse>> getProductVariationsByParentId(@PathVariable Long parentId) {
        return new ResponseEntity<>(this.productService.getProductVariationsByParentId(parentId),HttpStatus.OK);
    }



    @GetMapping("/internal-inventory/products/warehouse")
    public ResponseEntity<List<ProductInfoGetResponse>> filterProductInProductIdsByNameOrSku(
            @RequestParam(name = "name" , required = false) String name,
            @RequestParam(required = false) List<Long> productIds,
            @RequestParam(name = "sku", required = false) String sku

    ){
        return ResponseEntity.ok(this.productService.filterProductInProductIdsByNameOrSku(productIds,name,sku));



    }


    @GetMapping({"/internal-order/products"})
    public ResponseEntity<List<ProductCheckoutPreviewVm> > getProductCheckouts( @RequestParam(name = "ids") List<Long> productIds){
        return ResponseEntity.ok(this.productService.getProductCheckouts(productIds));
    }


    @PutMapping("/internal-order/products/subtract-quantity")
    public ResponseEntity<Void> subtractProductQuantityAfterOder(
            @Valid @RequestBody List<ProductSubtractQuantityRequest> productSubtractQuantityRequests
    ){
        this.productService.subtractProductQuantityAfterOder(productSubtractQuantityRequests);
        return ResponseEntity.noContent().build();
    }




}
