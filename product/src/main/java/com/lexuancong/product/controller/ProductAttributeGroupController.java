package com.lexuancong.product.controller;

import com.lexuancong.product.service.ProductAttributeGroupService;
import com.lexuancong.product.viewmodel.attributegroup.ProductAttributeGroupPostVm;
import com.lexuancong.product.viewmodel.attributegroup.ProductAttributeGroupVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ProductAttributeGroupController {
    private ProductAttributeGroupService productAttributeGroupService;
    public ProductAttributeGroupController(ProductAttributeGroupService productAttributeGroupService) {
        this.productAttributeGroupService = productAttributeGroupService;
    }

    @GetMapping({"/backoffice/product-attribute-group"})
    public ResponseEntity<List<ProductAttributeGroupVm>> getProductAttributeGroup() {
        List<ProductAttributeGroupVm> productAttributeGroupVms = this.productAttributeGroupService.getProductAttributeGroup();
        return new ResponseEntity<>(productAttributeGroupVms, HttpStatus.OK);
    }

    @PostMapping({"/backoffice/product-attribute-group"})
    public ResponseEntity<ProductAttributeGroupVm> createProductAttributeGroup(
            @Valid @RequestBody ProductAttributeGroupPostVm productAttributeGroupPostVm,
            // đuược inject dựa trên thông tin request hienej tại
            UriComponentsBuilder uriComponentsBuilder
            ) {
        ProductAttributeGroupVm productAttributeGroupVm = this.productAttributeGroupService
                .createProductAttributeGroup(productAttributeGroupPostVm);

    }



}
