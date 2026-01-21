package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.attribute.ProductAttribute;
import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import com.lexuancong.product.repository.ProductAttributeGroupRepository;
import com.lexuancong.product.repository.ProductAttributeRepository;
import com.lexuancong.product.dto.attribute.ProductAttributePagingGetResponse;
import com.lexuancong.product.dto.attribute.ProductAttributeCreateRequest;
import com.lexuancong.product.dto.attribute.ProductAttributeGetResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.NotFoundException;
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

    public List<ProductAttributeGetResponse> getProductAttributes() {
        return this.productAttributeRepository.findAll().stream()
                .map(ProductAttributeGetResponse::fromProductAttribute)
                .toList();
    }

    public ProductAttributePagingGetResponse getProductAttributePaging(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<ProductAttribute> productAttributePage = this.productAttributeRepository.findAll(pageable);
        List<ProductAttribute> productAttributes = productAttributePage.getContent();
        List<ProductAttributeGetResponse> productAttributeGetResponses = productAttributes.stream()
                .map(ProductAttributeGetResponse::fromProductAttribute)
                .toList();
        return new ProductAttributePagingGetResponse(
                productAttributeGetResponses,
                pageIndex,
                pageSize,
                (int) productAttributePage.getTotalElements(),
                productAttributePage.getTotalPages(),
                productAttributePage.isLast()
        );

    }

    public ProductAttribute createProductAttribute(ProductAttributeCreateRequest productAttributeCreateRequest) {
        this.validateExistedName(productAttributeCreateRequest.name(), null);

        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setName(productAttributeCreateRequest.name());
        this.setProductAttributeGroup(productAttribute, productAttributeCreateRequest.productAttributeGroupId());
        return this.productAttributeRepository.save(productAttribute);
    }

    private void setProductAttributeGroup(ProductAttribute productAttribute,Long productAttributeGroupId) {
        if(productAttributeGroupId != null) {
            ProductAttributeGroup productAttributeGroup = this.productAttributeGroupRepository
                    .findById(productAttributeGroupId)
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_GROUP_NOT_FOUND,productAttributeGroupId));
            productAttribute.setProductAttributeGroup(productAttributeGroup);
        }
    }


    private void validateExistedName(String name, Long id) {
        if (checkExistedName(name, id)) {
            throw new BadRequestException(Constants.ErrorKey.NAME_ALREADY_EXITED,name);
        }
    }

    private boolean checkExistedName(String name, Long id) {
        return this.productAttributeRepository.findByNameAndIdNot(name, id) != null;
    }

    public void updateProductAttribute(Long id, ProductAttributeCreateRequest productAttributeCreateRequest) {
        ProductAttribute productAttribute = this.productAttributeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_NOT_FOUND,id));
        this.validateExistedName(productAttributeCreateRequest.name(), id);
        productAttribute.setName(productAttributeCreateRequest.name());
        this.setProductAttributeGroup(productAttribute, productAttributeCreateRequest.productAttributeGroupId());
        this.productAttributeRepository.save(productAttribute);
    }


    public void deleteProductAttribute(Long id) {
        ProductAttribute productAttribute = productAttributeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_NOT_FOUND,id));


        if (!productAttribute.getProductAttributeValues().isEmpty()) {
            throw new BadRequestException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_CONSTANT_PRODUCT,id);
        }
        this.productAttributeRepository.deleteById(id);
    }
}
