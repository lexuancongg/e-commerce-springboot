package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductAttributeGroupService;
import com.lexuancong.product.dto.attributegroup.ProductAttributeGroupCreateRequest;
import com.lexuancong.product.dto.attributegroup.ProductAttributeGroupResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ProductAttributeGroupController {
    private ProductAttributeGroupService productAttributeGroupService;

    public ProductAttributeGroupController(ProductAttributeGroupService productAttributeGroupService) {
        this.productAttributeGroupService = productAttributeGroupService;
    }

    @GetMapping({"/management/product-attribute-groups"})
    public ResponseEntity<List<ProductAttributeGroupResponse>> getProductAttributeGroups() {
        List<ProductAttributeGroupResponse> attributeGroups = this.productAttributeGroupService.getProductAttributeGroups();
        return new ResponseEntity<>(attributeGroups, HttpStatus.OK);
    }

    @PostMapping({"/management/product-attribute-groups"})
    public ResponseEntity<ProductAttributeGroupResponse> createProductAttributeGroup(
            @Valid @RequestBody ProductAttributeGroupCreateRequest productAttributeGroupCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        ProductAttributeGroupResponse productAttributeGroupResponse = this.productAttributeGroupService
                .createProductAttributeGroup(productAttributeGroupCreateRequest);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("management/product-attribute-groups/{id}")
                .buildAndExpand(productAttributeGroupResponse.id()).toUri()
        ).body(productAttributeGroupResponse);

    }


    @PutMapping({"/management/product-attribute-groups/{id}"})
    public ResponseEntity<Void> updateProductAttributeGroup(@PathVariable Long id,
                                                            @Valid @RequestBody ProductAttributeGroupCreateRequest productAttributeGroupCreateRequest) {
        this.productAttributeGroupService.updateProductAttributeGroup(id, productAttributeGroupCreateRequest);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping({"/management/product-attribute-groups/{id}"})
    public ResponseEntity<Void> deleteProductAttributeGroup(@PathVariable Long id) {
        this.productAttributeGroupService.deleteProductAttributeGroup(id);
        return ResponseEntity.noContent().build();
    }


}
