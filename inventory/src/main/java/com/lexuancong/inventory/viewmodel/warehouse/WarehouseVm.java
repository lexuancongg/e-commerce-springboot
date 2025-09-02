package com.lexuancong.inventory.viewmodel.warehouse;

import com.lexuancong.inventory.model.Warehouse;

public record WarehouseVm(Long id, String name) {
    public static WarehouseVm fromModel(Warehouse warehouse) {
        return new WarehouseVm(warehouse.getId(), warehouse.getName());
    }
}
