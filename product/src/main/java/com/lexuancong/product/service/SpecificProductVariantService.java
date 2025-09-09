package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.SpecificProductVariant;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.repository.SpecificProductVariantRepository;
import com.lexuancong.product.viewmodel.productoptionvalue.ProductOptionValueGetVm;
import com.lexuancong.product.viewmodel.specificproductvariant.SpecificProductVariantGetVm;
import com.lexuancong.share.exception.BadRequestException;
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


    public List<ProductOptionValueGetVm> getProductOptionValuesOfSpecificProductVariants(List<Long> productIds){
        List<Product> products = productRepository.findAllById(productIds);
        if(products.size() != productIds.size()){
            throw  new BadRequestException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
        }
        List<SpecificProductVariant> specificProductVariants = specificProductVariantRepository.findAllByProductIn(products);
        return specificProductVariants.stream()
                .map(specificProductVariant -> {
                    return new ProductOptionValueGetVm(
                            specificProductVariant.getProduct().getId(),
                            specificProductVariant.getId(),
                            specificProductVariant.getProduct().getName(),
                            specificProductVariant.getProductOption().getId(),
                            specificProductVariant.getProductOption().getName(),
                            specificProductVariant.getValue()
                    );
                }).toList();


    }


}
