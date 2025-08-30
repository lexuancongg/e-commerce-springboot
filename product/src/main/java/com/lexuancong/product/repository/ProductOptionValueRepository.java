package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.ProductOptionValue;
import com.lexuancong.product.viewmodel.productoptionvalue.ProductOptionValueGetVm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {
    void deleteAllByProductId(Long productId);

    boolean existsByProductOption_Id(Long productOptionId);

    List<ProductOptionValue> findAllByProduct(Product product);
}
