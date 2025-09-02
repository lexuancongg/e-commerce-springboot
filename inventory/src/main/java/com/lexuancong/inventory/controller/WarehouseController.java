package com.lexuancong.inventory.controller;

import com.lexuancong.inventory.service.WarehouseService;
import com.lexuancong.inventory.viewmodel.warehouse.WarehousePostVm;
import com.lexuancong.inventory.viewmodel.warehouse.WarehouseVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;


    // checked
    @PostMapping("/management/warehouses")
    public ResponseEntity<WarehouseVm> createWarehouse(
            @RequestBody @Valid WarehousePostVm warehousePostVm,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        WarehouseVm warehouseVm = this.warehouseService.createWarehouse(warehousePostVm);
        return ResponseEntity.created(
                uriComponentsBuilder.replacePath("/warehouses/{id}")
                        .buildAndExpand(warehouseVm.id())
                        .toUri()
        ).body(warehouseVm);

    }


    // checked
    @PutMapping("/management/warehouses/{id}")
    public ResponseEntity<Void> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehousePostVm warehousePostVm
    ){
        this.warehouseService.updateWarehouse(id,warehousePostVm);
        return ResponseEntity.noContent().build();
    }



    // checked
    @DeleteMapping("/management/warehouses/{id}")
    public ResponseEntity<Void> deleteWarehouse(
            @PathVariable Long id
    ){
        this.warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }



    // checked
    @GetMapping("/management/warehouses")
    public ResponseEntity<List<WarehouseVm>> getWarehouses() {
        return ResponseEntity.ok(this.warehouseService.getWarehouses());

    }




}
