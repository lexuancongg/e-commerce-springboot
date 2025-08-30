package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductAttributeGroupService;
import com.lexuancong.product.viewmodel.attributegroup.ProductAttributeGroupPostVm;
import com.lexuancong.product.viewmodel.attributegroup.ProductAttributeGroupVm;
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

    // đã check
    @GetMapping({"/management/product-attribute-groups"})
    public ResponseEntity<List<ProductAttributeGroupVm>> getProductAttributeGroups() {
        List<ProductAttributeGroupVm> productAttributeGroupVms = this.productAttributeGroupService.getProductAttributeGroups();
        return new ResponseEntity<>(productAttributeGroupVms, HttpStatus.OK);
    }

    //đã check
    @PostMapping({"/management/product-attribute-groups"})
    public ResponseEntity<ProductAttributeGroupVm> createProductAttributeGroup(
            @Valid @RequestBody ProductAttributeGroupPostVm productAttributeGroupPostVm,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        ProductAttributeGroupVm productAttributeGroupVm = this.productAttributeGroupService
                .createProductAttributeGroup(productAttributeGroupPostVm);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("management/product-attribute-groups/{id}")
                .buildAndExpand(productAttributeGroupVm.id()).toUri()
        ).body(productAttributeGroupVm);

    }


    // đã check
    @PutMapping({"/management/product-attribute-groups/{id}"})
    public ResponseEntity<Void> updateProductAttributeGroup(@PathVariable Long id,
                                                            @Valid @RequestBody ProductAttributeGroupPostVm productAttributeGroupPostVm) {
        this.productAttributeGroupService.updateProductAttributeGroup(id, productAttributeGroupPostVm);
        return ResponseEntity.noContent().build();
    }

    // đã check

    @DeleteMapping({"/management/product-attribute-groups/{id}"})
    public ResponseEntity<Void> deleteProductAttributeGroup(@PathVariable Long id) {
        this.productAttributeGroupService.deleteProductAttributeGroup(id);
        return ResponseEntity.noContent().build();
    }


}
