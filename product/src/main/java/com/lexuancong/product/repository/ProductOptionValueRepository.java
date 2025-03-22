package com.lexuancong.product.repository;

import com.lexuancong.product.model.ProductOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {
    void deleteAllByProductId(Long productId);
}
