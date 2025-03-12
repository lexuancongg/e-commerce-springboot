package com.lexuancong.product.service;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.ProductAttributeValue;
import com.lexuancong.product.model.attribute.ProductAttribute;
import com.lexuancong.product.repository.ProductAttributeRepository;
import com.lexuancong.product.repository.ProductAttributeValueRepository;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.viewmodel.productattributevalue.ProductAttributeValuePostVm;
import com.lexuancong.product.viewmodel.productattributevalue.ProductAttributeValueVm;
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

    public List<ProductAttributeValueVm> getProductAttributeValueByProductId(Long productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException());
        List<ProductAttributeValueVm> productAttributeValueVms = this.productAttributeValueRepository
                .findAllByProduct(product)
                .stream()
                .map(ProductAttributeValueVm::fromModel)
                .toList();

        return productAttributeValueVms;

    }

    public ProductAttributeValueVm createProductAttributeValue(ProductAttributeValuePostVm productAttributeValuePostVm){
        ProductAttributeValue productAttributeValue = new ProductAttributeValue();
        if(productAttributeValuePostVm.productId()!=null){
            Product product = this.productRepository.findById(productAttributeValuePostVm.productId())
                    .orElseThrow(()->new RuntimeException());
            productAttributeValue.setProduct(product);
        }
        if(productAttributeValuePostVm.productAttributeId()!=null){
            ProductAttribute productAttribute = this.productAttributeRepository.
                    findById(productAttributeValuePostVm.productAttributeId())
                    .orElseThrow(()->new RuntimeException());
            productAttributeValue.setProductAttribute(productAttribute);
        }
        productAttributeValue.setValue(productAttributeValuePostVm.value());
        this.productAttributeValueRepository.save(productAttributeValue);
        return ProductAttributeValueVm.fromModel(productAttributeValue);

    }

    public void deleteProductAttributeValue(Long id){
        Optional<ProductAttributeValue> optionalProductAttributeValue = this.productAttributeValueRepository.findById(id);
        if(optionalProductAttributeValue.isEmpty()){
            throw new RuntimeException();
        }
        productAttributeRepository.deleteById(id);
    }

    public void updateProductAttributeValue(Long id,ProductAttributeValuePostVm productAttributeValuePostVm){
        Optional<ProductAttributeValue> optionalProductAttributeValue = this.productAttributeValueRepository.findById(id);
        if(optionalProductAttributeValue.isEmpty()){
            throw new RuntimeException();
        }
        ProductAttributeValue productAttributeValue = optionalProductAttributeValue.get();
        productAttributeValue.setValue(productAttributeValuePostVm.value());
        this.productAttributeValueRepository.save(productAttributeValue);


    }


}
