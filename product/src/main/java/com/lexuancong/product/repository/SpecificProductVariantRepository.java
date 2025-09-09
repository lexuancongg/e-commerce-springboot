package com.lexuancong.product.repository;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.SpecificProductVariant;
import com.lexuancong.product.viewmodel.specificproductvariant.SpecificProductVariantGetVm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SpecificProductVariantRepository extends JpaRepository<SpecificProductVariant, Long> {
    void deleteAllByProductIdIn(Collection<Long> productIds);

    List<SpecificProductVariant> findAllByProduct(Product product);

    List<SpecificProductVariant> findAllByProduct_Parent(Product productParent);

    List<SpecificProductVariant> findAllByProductIn(Collection<Product> products);
}
