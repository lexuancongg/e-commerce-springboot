package com.lexuancong.product.controller;

import com.lexuancong.product.model.attribute.ProductAttribute;
import com.lexuancong.product.service.ProductAttributeService;
import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.dto.attribute.ProductAttributePagingGetResponse;
import com.lexuancong.product.dto.attribute.ProductAttributeCreateRequest;
import com.lexuancong.product.dto.attribute.ProductAttributeGetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductAttributeController {
    private final ProductAttributeService productAttributeService;


    // đã check
    @GetMapping({"/management/product-attributes","customer/product-attributes"})
    public ResponseEntity<List<ProductAttributeGetResponse>> getProductAttributes() {
        return ResponseEntity.ok(this.productAttributeService.getProductAttributes());
    }

    // phân trang theo thuộc tính
    @GetMapping({"/management/product-attribute/paging"})
    public ResponseEntity<ProductAttributePagingGetResponse> getProductAttributePaging(
            @RequestParam(name ="pageIndex" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(name = "pageSize" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false)
            final int pageSize
    ) {
        return ResponseEntity.ok(this.productAttributeService.getProductAttributePaging(pageIndex, pageSize));
    }

    // đã check

    @PostMapping("/management/product-attributes")
    public ResponseEntity<ProductAttributeGetResponse> createProductAttribute(@RequestBody @Valid ProductAttributeCreateRequest productAttributeCreateRequest,
                                                                              UriComponentsBuilder uriComponentsBuilder ) {
        ProductAttribute productAttribute = this.productAttributeService.createProductAttribute(productAttributeCreateRequest);
        ProductAttributeGetResponse productAttributeGetResponse = ProductAttributeGetResponse.fromProductAttribute(productAttribute);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/product-attributes/{id}")
                        .buildAndExpand(productAttribute.getId()).toUri())
                .body(productAttributeGetResponse);
    }


    // đã check
    @PutMapping({"/management/product-attributes/{id}"})
    public ResponseEntity<Void> updateProductAttribute(@PathVariable Long id,
                                                       @Valid @RequestBody ProductAttributeCreateRequest productAttributeCreateRequest) {
        this.productAttributeService.updateProductAttribute(id, productAttributeCreateRequest);
        return ResponseEntity.noContent().build();
    }


    // đã check

    @DeleteMapping({"/management/product-attributes/{id}"})
    public ResponseEntity<Void> deleteProductAttribute(@PathVariable Long id) {
        this.productAttributeService.deleteProductAttribute(id);
        return ResponseEntity.noContent().build();
    }


}
