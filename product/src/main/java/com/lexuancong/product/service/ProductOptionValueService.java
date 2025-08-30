package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.repository.ProductOptionValueRepository;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.viewmodel.productoptionvalue.ProductOptionValueGetVm;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionValueService {
    private final ProductOptionValueRepository productOptionValueRepository;
    private final ProductRepository productRepository;

    public List<ProductOptionValueGetVm> getProductOptionValues(){
        return this.productOptionValueRepository.findAll()
                .stream()
                .map(ProductOptionValueGetVm::fromModel)
                .toList();
    }

    public List<ProductOptionValueGetVm> getProductOptionValuesByProductId(Long productId){
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND,productId));
        return this.productOptionValueRepository.findAllByProduct(product)
                .stream()
                .map(ProductOptionValueGetVm::fromModel)
                .toList();

    }
}
