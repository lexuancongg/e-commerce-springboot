package com.lexuancong.inventory.controller;

import com.lexuancong.inventory.service.WarehouseService;
import com.lexuancong.inventory.dto.warehouse.WarehouseCreateRequest;
import com.lexuancong.inventory.dto.warehouse.WarehouseGetResponse;
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
    public ResponseEntity<WarehouseGetResponse> createWarehouse(
            @RequestBody @Valid WarehouseCreateRequest warehouseCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        WarehouseGetResponse warehouseGetResponse = this.warehouseService.createWarehouse(warehouseCreateRequest);
        return ResponseEntity.created(
                uriComponentsBuilder.replacePath("/warehouses/{id}")
                        .buildAndExpand(warehouseGetResponse.id())
                        .toUri()
        ).body(warehouseGetResponse);

    }


    // checked
    @PutMapping("/management/warehouses/{id}")
    public ResponseEntity<Void> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseCreateRequest warehouseCreateRequest
    ){
        this.warehouseService.updateWarehouse(id, warehouseCreateRequest);
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
    public ResponseEntity<List<WarehouseGetResponse>> getWarehouses() {
        return ResponseEntity.ok(this.warehouseService.getWarehouses());

    }




}
