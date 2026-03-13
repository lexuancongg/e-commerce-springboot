package com.lexuancong.inventory.controller;

import com.lexuancong.inventory.service.WarehouseService;
import com.lexuancong.inventory.dto.warehouse.WarehouseCreateRequest;
import com.lexuancong.inventory.dto.warehouse.WarehouseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<WarehouseResponse> createWarehouse(
            @RequestBody @Valid WarehouseCreateRequest warehouseCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        WarehouseResponse warehouseResponse = this.warehouseService.createWarehouse(warehouseCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseResponse);

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
    public ResponseEntity<List<WarehouseResponse>> getWarehouses() {
        return ResponseEntity.ok(this.warehouseService.getWarehouses());

    }




}
