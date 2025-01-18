package com.lexuancong.product.repository;

import com.lexuancong.product.model.attribute.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Long> {
}
