package com.lexuancong.product.repository;

import com.lexuancong.product.model.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
     ProductOption findByNameAndIdNot(String name, Long id);
}
