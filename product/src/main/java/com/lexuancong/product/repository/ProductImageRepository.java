package com.lexuancong.product.repository;

import com.lexuancong.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteByProductId(Long productId);
    void deleteByImageIdInAndProductId(List<Long> imageIds, Long productId);

}
