package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductOptionCombinationRepository extends JpaRepository<ProductOptionCombination, Long> {
    void deleteAllByProductIdIn(Collection<Long> productIds);

    List<ProductOptionCombination> findAllByProduct(Product product);
}
