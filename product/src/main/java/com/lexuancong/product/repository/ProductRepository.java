package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    Optional<Product> findByGtin(String gtin);

    Optional<Product> findBySku(String sku);
    Page<Product> findAllByFeatureIsTrueAndShownSeparatelyIsTrueAndPublicIsTrueOrderByIdAsc(Pageable pageable);

    Optional<Product> findBySlugAndPublicIsTrue(String slug);




    @Query(value = "select product from Product product" +
            " where product.isFeature = true order by random() limit 10 ", nativeQuery = true)
    List<Product> findRandomFeaturedProducts();


    List<Product> findAllByFeatureIsTrue();


    List<Product> findAllByIdIn(Collection<Long> ids);
}
