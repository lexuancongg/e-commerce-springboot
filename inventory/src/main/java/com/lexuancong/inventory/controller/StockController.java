package com.lexuancong.inventory.controller;

import com.lexuancong.inventory.service.StockService;
import com.lexuancong.inventory.viewmodel.stock.StockGetVm;
import com.lexuancong.inventory.viewmodel.stock.StockPostVm;
import com.lexuancong.inventory.viewmodel.stock.StockPutQuantityVm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<Void> addProductIntoWarehouse(@RequestBody @NotEmpty @Valid List<StockPostVm> stockPostVmList) {
        this.stockService.addProductIntoWarehouse(stockPostVmList);
        return ResponseEntity.ok().build();
    }


    // checked

    @PutMapping
    private ResponseEntity<Void> updateQuantityProductInStock(@RequestBody @Valid List<StockPutQuantityVm> stockPutQuantityVms) {
        this.stockService.updateQuantityProductInStock(stockPutQuantityVms);
        return ResponseEntity.noContent().build();

    }


    // lấy danh sách để show => checked

    @GetMapping
    public ResponseEntity<List<StockGetVm>> getStockByWarehouseIdAndProductSkuAndProductName(
            @RequestParam(name = "warehouseId") Long warehouseId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productSku
    ){
        return ResponseEntity.ok(
                this.stockService.getStockByWarehouseIdAndProductSkuAndProductName(warehouseId,productSku,productName)
        );

    }



}
