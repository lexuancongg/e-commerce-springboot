package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.SpecificProductVariant;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.repository.SpecificProductVariantRepository;
import com.lexuancong.product.dto.productoptionvalue.ProductOptionValueGetResponse;
import com.lexuancong.product.dto.specificproductvariant.SpecificProductVariantGetResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecificProductVariantService {
    private final SpecificProductVariantRepository specificProductVariantRepository;
    private final ProductRepository productRepository;


    public List<SpecificProductVariantGetResponse> getSpecificProductVariantsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND,productId));

        return this.specificProductVariantRepository
                .findAllByProduct_Parent(product)
                .stream()
                .map(SpecificProductVariantGetResponse::fromSpecificProductVariant)
                .toList();
    }



    public List<ProductOptionValueGetResponse> getProductOptionValuesOfSpecificProductVariants(List<Long> productIds){
        List<Product> products = productRepository.findAllById(productIds);
        if(products.size() != productIds.size()){
            throw  new BadRequestException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
        }
        List<SpecificProductVariant> specificProductVariants = specificProductVariantRepository.findAllByProductIn(products);
        return specificProductVariants.stream()
                .map(specificProductVariant -> {
                    return new ProductOptionValueGetResponse(
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
