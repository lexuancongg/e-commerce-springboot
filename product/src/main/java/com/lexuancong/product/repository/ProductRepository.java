package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    Optional<Product> findByGtin(String gtin);

    Optional<Product> findBySku(String sku);
    Page<Product> findAllByFeatureIsTrueAndShownSeparatelyIsTrueAndPublicIsTrueOrderByIdAsc(Pageable pageable);

    Optional<Product> findBySlugAndPublicIsTrue(String slug);


}
