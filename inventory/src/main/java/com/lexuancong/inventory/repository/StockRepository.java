package com.lexuancong.inventory.repository;

import com.lexuancong.inventory.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    boolean findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    List<Stock> findAllByProductIdIn(Collection<Long> productIds);

    boolean existsByProductIdAndWarehouse_Id(Long productId, Long warehouseId);

    @Query("SELECT s FROM Stock s " +
            "WHERE (:warehouseId IS NULL OR s.warehouse.id = :warehouseId) " +
            "AND s.productId IN :productIds")
    List<Stock> findByWarehouseIdAndProductIds(@Param("warehouseId") Long warehouseId,
                                               @Param("productIds") Collection<Long> productIds);

}
