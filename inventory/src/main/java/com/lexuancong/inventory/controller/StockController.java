package com.lexuancong.inventory.controller;

import com.lexuancong.inventory.service.StockService;
import com.lexuancong.inventory.dto.stock.StockGetResponse;
import com.lexuancong.inventory.dto.stock.StockCreateRequest;
import com.lexuancong.inventory.dto.stock.StockPutQuantityRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;


    // checked
    @PostMapping
    public ResponseEntity<Void> addProductIntoWarehouse(@RequestBody @NotEmpty @Valid List<StockCreateRequest> stockCreateRequestList) {
        this.stockService.addProductIntoWarehouse(stockCreateRequestList);
        return ResponseEntity.ok().build();
    }


    // checked

    @PutMapping
    private ResponseEntity<Void> updateQuantityProductInStock(@RequestBody @Valid List<StockPutQuantityRequest> stockPutQuantityRequests) {
        this.stockService.updateQuantityProductInStock(stockPutQuantityRequests);
        return ResponseEntity.noContent().build();

    }


    // lấy danh sách để show => checked

    @GetMapping
    public ResponseEntity<List<StockGetResponse>> getStockByWarehouseIdAndProductSkuAndProductName(
            @RequestParam(name = "warehouseId") Long warehouseId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productSku
    ){
        return ResponseEntity.ok(
                this.stockService.getStockByWarehouseIdAndProductSkuAndProductName(warehouseId,productSku,productName)
        );

    }



}
