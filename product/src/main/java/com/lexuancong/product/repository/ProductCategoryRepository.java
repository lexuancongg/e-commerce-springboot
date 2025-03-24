package com.lexuancong.product.repository;

import com.lexuancong.product.model.Category;
import com.lexuancong.product.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findAllByProductId(Long productId);

    Page<ProductCategory> findAllByCategory(Category category, Pageable pageable);
}
