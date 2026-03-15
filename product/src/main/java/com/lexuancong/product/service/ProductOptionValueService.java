package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.repository.ProductOptionValueRepository;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.dto.productoptionvalue.ProductOptionValueResponse;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionValueService {
    private final ProductOptionValueRepository productOptionValueRepository;
    private final ProductRepository productRepository;

    public List<ProductOptionValueResponse> getProductOptionValues(){
        return this.productOptionValueRepository.findAll()
                .stream()
                .map(ProductOptionValueResponse::fromProductOptionValue)
                .toList();
    }

    public List<ProductOptionValueResponse> getProductOptionValuesByProductId(Long productId){
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND,productId));
        return this.productOptionValueRepository.findAllByProduct(product)
                .stream()
                .map(ProductOptionValueResponse::fromProductOptionValue)
                .toList();

    }
}
