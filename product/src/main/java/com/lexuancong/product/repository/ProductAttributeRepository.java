package com.lexuancong.product.repository;

import com.lexuancong.product.model.attribute.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Long> {
    @Query("select productAttribute from ProductAttribute productAttribute " +
            "where productAttribute.name =?1 and (?2 is null or productAttribute.id != ?2)")
    ProductAttribute findExistedName(String name,Long id);
}
