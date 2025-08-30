package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.ProductOption;
import com.lexuancong.product.repository.ProductOptionRepository;
import com.lexuancong.product.repository.ProductOptionValueRepository;
import com.lexuancong.product.viewmodel.productoptions.ProductOptionGetVm;
import com.lexuancong.product.viewmodel.productoptions.ProductOptionPostVm;
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

    public List<ProductOptionGetVm> getProductOptions() {
        List<ProductOption> productOptions = productOptionRepository.findAll();
        return productOptions.stream()
                .map(ProductOptionGetVm::fromModel)
                .collect(Collectors.toList());
    }

    public ProductOptionGetVm createProductOption(ProductOptionPostVm productOptionPostVm){
        this.validateDuplicatedName(productOptionPostVm.name(), null);
        ProductOption productOption = productOptionPostVm.toModel();
        productOptionRepository.save(productOption);
        return ProductOptionGetVm.fromModel(productOption);

    }

    private void validateDuplicatedName(String name, Long id){
        if(this.checkExitedName(name,id)){
            throw new BadRequestException(Constants.ErrorKey.NAME_ALREADY_EXITED,name);
        }
    }
    private boolean checkExitedName(String name,Long id) {
        return  this.productOptionRepository.findByNameAndIdNot(name, id)!= null;
    }


    public void updateProductOption(Long id, ProductOptionPostVm productOptionPostVm){
        ProductOption productOption = this.productOptionRepository.findById(id)
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.PRODUCT_OPTION_NOT_FOUND,id));
        this.validateDuplicatedName(productOptionPostVm.name(), id);
        productOption.setName(productOptionPostVm.name());
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
