package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.ProductAttributeValue;
import com.lexuancong.product.model.attribute.ProductAttribute;
import com.lexuancong.product.repository.ProductAttributeRepository;
import com.lexuancong.product.repository.ProductAttributeValueRepository;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.dto.productattribute.ProductAttributeValueCreateRequest;
import com.lexuancong.product.dto.productattribute.ProductAttributeValueGetResponse;
import com.lexuancong.share.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductAttributeValueService {
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductRepository productRepository;
    private final ProductAttributeRepository productAttributeRepository;

    public ProductAttributeValueService(ProductAttributeValueRepository productAttributeValueRepository, ProductRepository productRepository, ProductAttributeRepository productAttributeRepository) {
        this.productAttributeValueRepository = productAttributeValueRepository;
        this.productRepository = productRepository;
        this.productAttributeRepository = productAttributeRepository;
    }

    public List<ProductAttributeValueGetResponse> getProductAttributeValueByProductId(Long productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, productId));

        List<ProductAttributeValueGetResponse> productAttributeValueGetResponses = this.productAttributeValueRepository
                .findAllByProduct(product)
                .stream()
                .map(ProductAttributeValueGetResponse::fromProductAttributeValue)
                .toList();

        return productAttributeValueGetResponses;

    }

    public ProductAttributeValueGetResponse createProductAttributeValue(ProductAttributeValueCreateRequest productAttributeValueCreateRequest) {
        ProductAttributeValue productAttributeValue = new ProductAttributeValue();
        Product product = this.productRepository.findById(productAttributeValueCreateRequest.productId())
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, productAttributeValueCreateRequest.productId()));
        productAttributeValue.setProduct(product);
        ProductAttribute productAttribute = this.productAttributeRepository.
                findById(productAttributeValueCreateRequest.productAttributeId())
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_NOT_FOUND, productAttributeValueCreateRequest.productAttributeId()));
        productAttributeValue.setProductAttribute(productAttribute);
        productAttributeValue.setValue(productAttributeValueCreateRequest.value());
        this.productAttributeValueRepository.save(productAttributeValue);
        return ProductAttributeValueGetResponse.fromProductAttributeValue(productAttributeValue);

    }

    public void deleteProductAttributeValue(Long id) {
        Optional<ProductAttributeValue> optionalProductAttributeValue = this.productAttributeValueRepository.findById(id);
        if (optionalProductAttributeValue.isEmpty()) {
            throw new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_VALUE_NOT_FOUND, id);
        }
        productAttributeRepository.deleteById(id);
    }

    public void updateProductAttributeValue(Long id, ProductAttributeValueCreateRequest productAttributeValueCreateRequest) {
        Optional<ProductAttributeValue> optionalProductAttributeValue = this.productAttributeValueRepository.findById(id);
        if (optionalProductAttributeValue.isEmpty()) {
            throw new NotFoundException(Constants.ErrorKey.PRODUCT_ATTRIBUTE_VALUE_NOT_FOUND, id);
        }
        ProductAttributeValue productAttributeValue = optionalProductAttributeValue.get();
        productAttributeValue.setValue(productAttributeValueCreateRequest.value());
        this.productAttributeValueRepository.save(productAttributeValue);


    }


}
