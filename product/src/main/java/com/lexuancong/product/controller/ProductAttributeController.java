package com.lexuancong.product.controller;

import com.lexuancong.product.model.attribute.ProductAttribute;
import com.lexuancong.product.service.ProductAttributeService;
import com.lexuancong.product.utils.Constants;
import com.lexuancong.product.viewmodel.attribute.ProductAttributePagingVm;
import com.lexuancong.product.viewmodel.attribute.ProductAttributePostVm;
import com.lexuancong.product.viewmodel.attribute.ProductAttributeVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductAttributeController {
    private final ProductAttributeService productAttributeService;


    @GetMapping({"/management/product-attribute","customer/product-attribute"})
    public ResponseEntity<List<ProductAttributeVm>> getProductAttributes() {
        return ResponseEntity.ok(this.productAttributeService.getProductAttributes());
    }

    // phân trang theo thuộc tính
    @GetMapping({"/management/product-attribute/paging"})
    public ResponseEntity<ProductAttributePagingVm> getProductAttributePaging(
            @RequestParam(name ="pageIndex" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(name = "pageSize" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false)
            final int pageSize
    ) {
        return ResponseEntity.ok(this.productAttributeService.getProductAttributePaging(pageIndex, pageSize));
    }


    @PostMapping("/management/product-attribute")
    public ResponseEntity<ProductAttributeVm> createProductAttribute(@RequestBody @Valid ProductAttributePostVm productAttributePostVm,
                                                                     UriComponentsBuilder uriComponentsBuilder ) {
        ProductAttribute productAttribute = this.productAttributeService.createProductAttribute(productAttributePostVm);
        ProductAttributeVm productAttributeVm = ProductAttributeVm.fromModel(productAttribute);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/product-attribute/{id}")
                        .buildAndExpand(productAttribute.getId()).toUri())
                .body(productAttributeVm);
    }

    @PutMapping({"/management/product-attribute/{id}"})
    public ResponseEntity<Void> updateProductAttribute(@PathVariable Long id,
                                                       @Valid @RequestBody ProductAttributePostVm productAttributePostVm) {
        this.productAttributeService.updateProductAttribute(id,productAttributePostVm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping({"/management/product-attribute/{id}"})
    public ResponseEntity<Void> deleteProductAttribute(@PathVariable Long id) {
        this.productAttributeService.deleteProductAttribute(id);
        return ResponseEntity.noContent().build();
    }


}
