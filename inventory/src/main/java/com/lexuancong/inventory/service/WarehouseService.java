package com.lexuancong.inventory.service;

import com.lexuancong.inventory.repository.WarehouseRepository;
import com.lexuancong.inventory.viewmodel.product.ProductInfoVm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    public List<Long> getProductIdsInWarehouse(Long warehouseId) {
        return this.warehouseRepository.getProductIdsInWarehouse(warehouseId);

    }
}
