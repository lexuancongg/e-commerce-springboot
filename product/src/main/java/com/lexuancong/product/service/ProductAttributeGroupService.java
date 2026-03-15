package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import com.lexuancong.product.repository.ProductAttributeGroupRepository;
import com.lexuancong.product.dto.attributegroup.ProductAttributeGroupCreateRequest;
import com.lexuancong.product.dto.attributegroup.ProductAttributeGroupResponse;
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


    public List<ProductAttributeGroupResponse> getProductAttributeGroups(){
        return this.productAttributeGroupRepository.findAll()
                .stream()
                .map(ProductAttributeGroupResponse::fromProductAttributeGroup)
                .toList();
    }


    public ProductAttributeGroupResponse createProductAttributeGroup(ProductAttributeGroupCreateRequest productAttributeGroupCreateRequest){
        ProductAttributeGroup productAttributeGroup = productAttributeGroupCreateRequest.toProductAttributeGroup();

        this.validateDuplicateName(null, productAttributeGroupCreateRequest.name());
        this.productAttributeGroupRepository.save(productAttributeGroup);
        return ProductAttributeGroupResponse.fromProductAttributeGroup(productAttributeGroup);

    }
    public void validateDuplicateName(Long id, String name){
        if(this.checkExitsName(id,name)){
             throw new BadRequestException(Constants.ErrorKey.NAME_ALREADY_EXITED,name);
        }
    }
    public boolean checkExitsName(Long id, String name){
        return this.productAttributeGroupRepository.findExistedName(id,name)!=null;
    }


    public void updateProductAttributeGroup(Long id, ProductAttributeGroupCreateRequest productAttributeGroupCreateRequest){
        String attributeGroupName = productAttributeGroupCreateRequest.name();
        ProductAttributeGroup productAttributeGroup = this.productAttributeGroupRepository
                .findById(id)
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_GROUP_NOT_FOUND,id));
        this.validateDuplicateName(id, attributeGroupName);
        productAttributeGroup.setName(attributeGroupName);
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
