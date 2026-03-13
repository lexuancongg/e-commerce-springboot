package com.lexuancong.inventory.dto.warehouse;

import com.lexuancong.inventory.model.Warehouse;

public record WarehouseResponse(Long id, String name) {
    public static WarehouseResponse fromWarehouse(Warehouse warehouse) {
        return new WarehouseResponse(warehouse.getId(), warehouse.getName());
    }
}
