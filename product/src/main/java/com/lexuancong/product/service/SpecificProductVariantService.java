package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.repository.SpecificProductVariantRepository;
import com.lexuancong.product.viewmodel.specificproductvariant.SpecificProductVariantGetVm;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.constant.Constable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecificProductVariantService {
    private final SpecificProductVariantRepository specificProductVariantRepository;
    private final ProductRepository productRepository;


    public List<SpecificProductVariantGetVm> getSpecificProductVariantsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND,productId));
        List<SpecificProductVariantGetVm> specificProductVariantGetVms = this.specificProductVariantRepository
                .findAllByProduct_Parent(product)
                .stream()
                .map(SpecificProductVariantGetVm::fromModel)
                .toList();

        return specificProductVariantGetVms;
    }

}
