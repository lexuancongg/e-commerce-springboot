package com.lexuancong.product.repository;

import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductAttributeGroupRepository extends JpaRepository<ProductAttributeGroup, Long> {
    @Query("select productAttributeGroup from ProductAttributeGroup productAttributeGroup " +
            "where productAttributeGroup.name = ?2 and (productAttributeGroup.id is null or productAttributeGroup.id != ?1)")
    ProductAttributeGroup findExistedName(Long id,String name);
}
