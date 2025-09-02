package com.lexuancong.inventory.repository;

import com.lexuancong.inventory.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("""
    select s.productId from Stock s
    where (:warehouseId is null or s.warehouse.id = :warehouseId)
""")
    List<Long> getProductIdsInWarehouse(@Param("warehouseId") Long warehouseId);

}
