package com.lexuancong.product.service;

import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.viewmodel.product.post.ProductPostVm;
import com.lexuancong.product.viewmodel.product.post.ProductSummaryVm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductSummaryVm createProduct(ProductPostVm productPostVm){

    }

}
