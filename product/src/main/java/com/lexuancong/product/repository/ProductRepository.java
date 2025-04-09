package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    Optional<Product> findByGtin(String gtin);

    Optional<Product> findBySku(String sku);
    Page<Product> findAllByFeatureIsTrueAndShownSeparatelyIsTrueAndPublicIsTrueOrderByIdAsc(Pageable pageable);

    Optional<Product> findBySlugAndPublicIsTrue(String slug);
git

    @Query(value = "select product from Product product" +
            " where product.isFeature = true order by random() limit 10 ", nativeQuery = true)
    List<Product> findRandomFeaturedProducts();


    List<Product> findAllByFeatureIsTrue();


    List<Product> findAllByIdIn(Collection<Long> ids);

    @Query(value = "select product from Product product left join " +
            "product.productCategories productCategories  left join productCategories.category category " +
            "where lower(product.name) like %:productName% " +
            "and (category.slug = :categorySlug or (category.slug is null  or category.slug =''))" +
            "and (:startPrice is null or product.price >= :startPrice )" +
            "and (:endPrice is null  or product.price <= :endPrice)" +
            "and  product.isPublic = true " +
            "and product.isShownSeparately = true " +
            "order by product.id asc "
    )
    Page<Product> findByProductNameAndCategorySlugAndPriceBetween(@Param("productName") String productName,
                                                                  @Param("categorySlug") String categorySlug,
                                                                  @Param("startPrice") Double startPrice,
                                                                  @Param("endPrice") Double endPrice,
                                                                  Pageable pageable);
}
