package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
    List<ProductAttributeValue> findAllByProduct(Product product);
}
