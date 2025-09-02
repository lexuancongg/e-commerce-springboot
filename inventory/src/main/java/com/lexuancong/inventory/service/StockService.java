package com.lexuancong.inventory.service;

import com.lexuancong.inventory.constants.Constants;
import com.lexuancong.inventory.model.Stock;
import com.lexuancong.inventory.model.Warehouse;
import com.lexuancong.inventory.repository.StockRepository;
import com.lexuancong.inventory.repository.WarehouseRepository;
import com.lexuancong.inventory.service.Internal.ProductService;
import com.lexuancong.inventory.viewmodel.product.ProductInfoVm;
import com.lexuancong.inventory.viewmodel.stock.StockGetVm;
import com.lexuancong.inventory.viewmodel.stock.StockPostVm;
import com.lexuancong.inventory.viewmodel.stock.StockPutQuantityVm;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final ProductService productService;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseService warehouseService;

    public StockService(StockRepository stockRepository, ProductService productService, WarehouseRepository warehouseRepository, WarehouseService warehouseService) {
        this.stockRepository = stockRepository;
        this.productService = productService;
        this.warehouseRepository = warehouseRepository;
        this.warehouseService = warehouseService;
    }


    public void addProductIntoWarehouse(List<StockPostVm> stockPostVmList){
        List<Stock> stocks = new ArrayList<>();
        for (StockPostVm stockPostVm : stockPostVmList) {

            // cần update sau vì for lớn gây ra performan kém
            ProductInfoVm productInfoVm = this.productService.getProductById(stockPostVm.productId());
            if(productInfoVm == null){
                throw new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, stockPostVm.productId());
            }
            Warehouse warehouse = this.warehouseRepository.findById(stockPostVm.warehouseId())
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND, stockPostVm.warehouseId()));
            this.validateExitedProductInStock(stockPostVm.productId(),stockPostVm.warehouseId());

            Stock stock = Stock.builder()
                    .productId(stockPostVm.productId())
                    .quantity(0)
                    .warehouse(warehouse)
                    .lockedQuantity(0)
                    .build();
            stocks.add(stock);

        }
        this.stockRepository.saveAll(stocks);
    }

    private void  validateExitedProductInStock(Long productId, Long warehouseId){
        if(this.checkExitedProductInStock(productId, warehouseId)){
            throw new DuplicatedException(Constants.ErrorKey.STOCK_ALREADY_EXIST);
        }
    }
    private boolean checkExitedProductInStock(Long productId, Long warehouseId){
        return this.stockRepository.existsByProductIdAndWarehouse_Id(productId, warehouseId);
    }


    public void updateQuantityProductInStock(List<StockPutQuantityVm> stockPutQuantityVms){
        List<Long> productIds = stockPutQuantityVms.parallelStream()
                .map(StockPutQuantityVm::productId)
                .toList();

        List<Long> stockIds = stockPutQuantityVms.parallelStream()
                .map(StockPutQuantityVm::stockId)
                .toList();

        List<Stock> stocks = this.stockRepository.findAllById(stockIds);

        Map<Long,Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getId, stock -> stock));

        for (StockPutQuantityVm stockPutQuantityVm : stockPutQuantityVms) {
            Stock stock = stockMap.get(stockPutQuantityVm.stockId());
            if(stock == null)
                continue;
            stock.setQuantity(stockPutQuantityVm.quantity());
        }
        // tham chiếu nên stocks được cập nhật
        this.stockRepository.saveAll(stocks);
    }


    public List<StockGetVm> getStockByWarehouseIdAndProductSkuAndProductName(Long warehouseId, String productSku,String productName){
        List<Long> productIdsInWarehouse = this.warehouseService.getProductIdsInWarehouse(warehouseId);
        // tìm kiếm trong productId này có product nào có sku vaf name mach nếu k null
        List<ProductInfoVm> productInfoVms = this.productService.filterProductInProductIdsByNameAndSku(productIdsInWarehouse,productSku,productName);


    }
}
