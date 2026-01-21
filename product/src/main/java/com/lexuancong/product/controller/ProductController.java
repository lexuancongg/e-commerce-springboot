package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductService;
import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.dto.product.*;
import com.lexuancong.product.dto.product.producforwarehouse.ProductInfoGetResponse;
import com.lexuancong.product.dto.product.variants.ProductVariantGetResponse;
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

    // create => đã check
    @PostMapping({"/management/products"})
    public ResponseEntity<ProductSummaryGetResponse> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        ProductSummaryGetResponse productSummaryGetResponse = this.productService.createProduct(productCreateRequest);
        return new ResponseEntity<>(productSummaryGetResponse, HttpStatus.CREATED);

    }

    // update // đã check
    @PutMapping(path = "/management/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProductCreateRequest productCreateRequest) {
        this.productService.updateProduct(id, productCreateRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    // ds nổi bật => CHECKED
    @GetMapping("/customer/products/featured")
    public ResponseEntity<ProductPreviewPagingGetResponse> getFeaturedProductsPaging(
            @RequestParam(value = "pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        return ResponseEntity.ok(productService.getFeaturedProductsPaging(pageIndex,pageSize));
    }


    // xem chi tiết sp => đã check
    @GetMapping("/customer/products/{slug}")
    public ResponseEntity<ProductDetailGetResponse> getProductDetail(@PathVariable("slug") String slug) {
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
    public ResponseEntity<ProductPagingGetResponse> getProductsFromCategoryPaging(
            @RequestParam(value = "pageIndex",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @PathVariable String categorySlug
    ){
        return ResponseEntity.ok(this.productService.getProductsByCategorySlug(pageIndex,pageSize,categorySlug));
    }


    //  lấy ngaaux nhiên sp làm slide => đã check
    @GetMapping({"/customer/products/featured/slide"})
    public ResponseEntity<List<ProductPreviewGetResponse>> getProductFeaturedMakeSlide(){
        List<ProductPreviewGetResponse> productPreviewGetResponses =  productService.getProductFeaturedMakeSlide();
        return new ResponseEntity<>(productPreviewGetResponses, HttpStatus.OK);
    }


    // đã check
    @GetMapping({"/customer/products","/internal-order/products"})
    public ResponseEntity<List<ProductPreviewGetResponse>> getProductsByIds (
            @RequestParam("productIds") List<Long> productIds
    ){
        return ResponseEntity.ok(this.productService.getProductsByIds(productIds));
    }




    @GetMapping("/customer/products")
    public ResponseEntity<ProductPagingGetResponse> getProductByMultiParams(
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
    public ResponseEntity<List<ProductVariantGetResponse>> getProductVariationsByParentId(@PathVariable Long parentId) {
        return new ResponseEntity<>(this.productService.getProductVariationsByParentId(parentId),HttpStatus.OK);
    }



    // checked
    @GetMapping("/internal/products/warehouse")
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
            @Valid @RequestBody List<ProductSubtractQuantityVm> productSubtractQuantityVms
    ){
        this.productService.subtractProductQuantityAfterOder(productSubtractQuantityVms);
        return ResponseEntity.noContent().build();
    }




}
