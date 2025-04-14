package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.SpecificProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductOptionCombinationRepository extends JpaRepository<SpecificProductVariant, Long> {
    void deleteAllByProductIdIn(Collection<Long> productIds);

    List<SpecificProductVariant> findAllByProduct(Product product);
}
