package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.ProductOption;
import com.lexuancong.product.repository.ProductOptionRepository;
import com.lexuancong.product.repository.ProductOptionValueRepository;
import com.lexuancong.product.dto.productoptions.ProductOptionGetResponse;
import com.lexuancong.product.dto.productoptions.ProductOptionCreateRequest;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionValueRepository productOptionValueRepository;

    public List<ProductOptionGetResponse> getProductOptions() {
        List<ProductOption> productOptions = productOptionRepository.findAll();
        return productOptions.stream()
                .map(ProductOptionGetResponse::fromProductOption)
                .collect(Collectors.toList());
    }

    public ProductOptionGetResponse createProductOption(ProductOptionCreateRequest productOptionCreateRequest){
        this.validateDuplicatedName(productOptionCreateRequest.name(), null);
        ProductOption productOption = productOptionCreateRequest.toProductOption();
        productOptionRepository.save(productOption);
        return ProductOptionGetResponse.fromProductOption(productOption);

    }

    private void validateDuplicatedName(String name, Long id){
        if(this.checkExitedName(name,id)){
            throw new BadRequestException(Constants.ErrorKey.NAME_ALREADY_EXITED,name);
        }
    }
    private boolean checkExitedName(String name,Long id) {
        return  this.productOptionRepository.findByNameAndIdNot(name, id)!= null;
    }


    public void updateProductOption(Long id, ProductOptionCreateRequest productOptionCreateRequest){
        ProductOption productOption = this.productOptionRepository.findById(id)
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.PRODUCT_OPTION_NOT_FOUND,id));
        this.validateDuplicatedName(productOptionCreateRequest.name(), id);
        productOption.setName(productOptionCreateRequest.name());
        this.productOptionRepository.save(productOption);

    }

    public void deleteProductOption(Long id){
        ProductOption productOption = this.productOptionRepository.findById(id)
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.PRODUCT_OPTION_NOT_FOUND,id));
        if(this.productOptionValueRepository.existsByProductOption_Id(productOption.getId())){
            throw new BadRequestException(Constants.ErrorKey.PRODUCT_OPTION_CONSTANT_PRODUCT_OPTION_VALUE,id);
        }
        this.deleteProductOption(id);
    }
}
