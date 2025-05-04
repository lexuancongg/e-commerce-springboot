package com.lexuancong.inventory.repository;

import com.lexuancong.inventory.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    boolean findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    List<Stock> findAllByProductIdIn(Collection<Long> productIds);
}
