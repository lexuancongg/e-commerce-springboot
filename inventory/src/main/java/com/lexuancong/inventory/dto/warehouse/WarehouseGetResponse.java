package com.lexuancong.inventory.dto.warehouse;

import com.lexuancong.inventory.model.Warehouse;

public record WarehouseGetResponse(Long id, String name) {
    public static WarehouseGetResponse fromWarehouse(Warehouse warehouse) {
        return new WarehouseGetResponse(warehouse.getId(), warehouse.getName());
    }
}
