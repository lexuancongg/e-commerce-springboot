package com.lexuancong.product.service;

import com.lexuancong.product.model.attribute.ProductAttribute;
import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import com.lexuancong.product.repository.ProductAttributeGroupRepository;
import com.lexuancong.product.repository.ProductAttributeRepository;
import com.lexuancong.product.viewmodel.attribute.ProductAttributePagingVm;
import com.lexuancong.product.viewmodel.attribute.ProductAttributePostVm;
import com.lexuancong.product.viewmodel.attribute.ProductAttributeVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeService {
    private final ProductAttributeGroupRepository productAttributeGroupRepository;
    private final ProductAttributeRepository productAttributeRepository;
    public ProductAttributeService(ProductAttributeGroupRepository productAttributeGroupRepository, ProductAttributeRepository productAttributeRepository) {
        this.productAttributeGroupRepository = productAttributeGroupRepository;
        this.productAttributeRepository = productAttributeRepository;
    }

    public List<ProductAttributeVm> getProductAttributes() {
        return this.productAttributeRepository.findAll().stream()
                .map(ProductAttributeVm::fromModel)
                .toList();
    }

    public ProductAttributePagingVm getProductAttributePaging(int pageIndex,int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<ProductAttribute>  productAttributePage = this.productAttributeRepository.findAll(pageable);
        List<ProductAttribute> productAttributes = productAttributePage.getContent();
        List<ProductAttributeVm> productAttributeVms = productAttributes.stream()
                .map(ProductAttributeVm::fromModel)
                .toList();
        return new ProductAttributePagingVm(
                productAttributeVms,
                pageIndex,
                pageSize,
                (int) productAttributePage.getTotalElements(),
                productAttributePage.getTotalPages(),
                productAttributePage.isLast()
        );

    }

    public ProductAttribute createProductAttribute(ProductAttributePostVm productAttributePostVm) {
        this.validateExistedName(productAttributePostVm.name(),null);
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setName(productAttributePostVm.name());
        if(productAttributePostVm.productAttributeGroupId() != null) {
            ProductAttributeGroup productAttributeGroup = this.productAttributeGroupRepository
                    .findById(productAttributePostVm.productAttributeGroupId())
                    // throw lỗi
                    .orElseThrow(()-> new RuntimeException());
            productAttribute.setProductAttributeGroup(productAttributeGroup);
        }
        return this.productAttributeRepository.save(productAttribute);
    }


    private void validateExistedName(String name,Long id){
        if(checkExistedName(name,id)){
            // băn ngoại le
        }
    }
    private boolean checkExistedName(String name,Long id){
        return this.productAttributeRepository.findExistedName(name,id)!=null;
    }

    public void updateProductAttribute(Long id, ProductAttributePostVm productAttributePostVm) {
        this.validateExistedName(productAttributePostVm.name(),id);
        ProductAttribute productAttribute = this.productAttributeRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException());
        if(productAttributePostVm.productAttributeGroupId() != null) {
            ProductAttributeGroup productAttributeGroup = this.productAttributeGroupRepository
                    .findById(productAttributePostVm.productAttributeGroupId())
                    .orElseThrow(()-> new RuntimeException());
            productAttribute.setProductAttributeGroup(productAttributeGroup);
        }
        this.productAttributeRepository.save(productAttribute);
    }


    public void deleteProductAttribute(Long id){
        ProductAttribute productAttribute = productAttributeRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException());
        if(!productAttribute.getProductAttributeValues().isEmpty()){
            throw new RuntimeException();
        }
        this.productAttributeRepository.deleteById(id);
    }
}
