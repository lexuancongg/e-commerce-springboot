package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import com.lexuancong.product.repository.ProductAttributeGroupRepository;
import com.lexuancong.product.dto.attributegroup.ProductAttributeGroupCreateRequest;
import com.lexuancong.product.dto.attributegroup.ProductAttributeGroupGetResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeGroupService {
    private final ProductAttributeGroupRepository productAttributeGroupRepository;

    public ProductAttributeGroupService(ProductAttributeGroupRepository productAttributeGroupRepository) {
        this.productAttributeGroupRepository = productAttributeGroupRepository;
    }


    public List<ProductAttributeGroupGetResponse> getProductAttributeGroups(){
        return this.productAttributeGroupRepository.findAll()
                .stream()
                .map(ProductAttributeGroupGetResponse::fromProductAttributeGroup)
                .toList();
    }


    public ProductAttributeGroupGetResponse createProductAttributeGroup(ProductAttributeGroupCreateRequest productAttributeGroupCreateRequest){
        ProductAttributeGroup productAttributeGroup = productAttributeGroupCreateRequest.toProductAttributeGroup();

        this.validateExistedName(null, productAttributeGroupCreateRequest.name());
        this.productAttributeGroupRepository.save(productAttributeGroup);
        return ProductAttributeGroupGetResponse.fromProductAttributeGroup(productAttributeGroup);

    }
    public void validateExistedName(Long id,String name){
        if(this.checkExistedName(id,name)){
             throw new BadRequestException(Constants.ErrorKey.NAME_ALREADY_EXITED,name);
        }
    }
    public boolean checkExistedName(Long id,String name){
        return this.productAttributeGroupRepository.findExistedName(id,name)!=null;
    }


    public void updateProductAttributeGroup(Long id, ProductAttributeGroupCreateRequest productAttributeGroupCreateRequest){
        ProductAttributeGroup productAttributeGroup = this.productAttributeGroupRepository
                .findById(id)
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_GROUP_NOT_FOUND,id));
        productAttributeGroup.setName(productAttributeGroupCreateRequest.name());
        this.productAttributeGroupRepository.save(productAttributeGroup);


    }
    public void deleteProductAttributeGroup(long id){
        ProductAttributeGroup productAttributeGroup = this.productAttributeGroupRepository.findById(id)
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_GROUP_NOT_FOUND,id));
        if(!productAttributeGroup.getProductAttributes().isEmpty()){
            throw new BadRequestException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_GROUP_CONSTANT_ATTRIBUTE,id);
        }
        this.productAttributeGroupRepository.deleteById(id);
    }
}
