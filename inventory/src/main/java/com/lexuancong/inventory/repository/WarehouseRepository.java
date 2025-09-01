package com.lexuancong.inventory.repository;

import com.lexuancong.inventory.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
