package com.lexuancong.inventory.service;

import com.lexuancong.inventory.constants.Constants;
import com.lexuancong.inventory.model.Stock;
import com.lexuancong.inventory.model.Warehouse;
import com.lexuancong.inventory.repository.StockRepository;
import com.lexuancong.inventory.repository.WarehouseRepository;
import com.lexuancong.inventory.service.Internal.ProductService;
import com.lexuancong.inventory.dto.product.ProductInfoGetResponse;
import com.lexuancong.inventory.dto.stock.StockGetResponse;
import com.lexuancong.inventory.dto.stock.StockCreateRequest;
import com.lexuancong.inventory.dto.stock.StockPutQuantityRequest;
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


    public void addProductIntoWarehouse(List<StockCreateRequest> stockCreateRequestList){
        List<Stock> stocks = new ArrayList<>();
        for (StockCreateRequest stockCreateRequest : stockCreateRequestList) {

            // cần update sau vì for lớn gây ra performan kém
            ProductInfoGetResponse productInfoGetResponse = this.productService.getProductById(stockCreateRequest.productId());
            if(productInfoGetResponse == null){
                throw new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, stockCreateRequest.productId());
            }
            Warehouse warehouse = this.warehouseRepository.findById(stockCreateRequest.warehouseId())
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND, stockCreateRequest.warehouseId()));
            this.validateExitedProductInStock(stockCreateRequest.productId(), stockCreateRequest.warehouseId());

            Stock stock = Stock.builder()
                    .productId(stockCreateRequest.productId())
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


    public void updateQuantityProductInStock(List<StockPutQuantityRequest> stockPutQuantityRequests){

        List<Long> stockIds = stockPutQuantityRequests.parallelStream()
                .map(StockPutQuantityRequest::stockId)
                .toList();

        List<Stock> stocks = this.stockRepository.findAllById(stockIds);

        Map<Long,Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getId, stock -> stock));

        for (StockPutQuantityRequest stockPutQuantityRequest : stockPutQuantityRequests) {
            Stock stock = stockMap.get(stockPutQuantityRequest.stockId());
            if(stock == null)
                continue;
            stock.setQuantity(stockPutQuantityRequest.quantity());
        }
        this.stockRepository.saveAll(stocks);
    }


    public List<StockGetResponse> getStockByWarehouseIdAndProductSkuAndProductName(Long warehouseId, String productSku, String productName){
        List<Long> productIdsInWarehouse = this.warehouseService.getProductIdsInWarehouse(warehouseId);
        // tìm kiếm trong productId này có product nào có sku vaf name mach nếu k null
        List<ProductInfoGetResponse> productInfoGetResponses = this.productService.filterProductInProductIdsByNameOrSku(productIdsInWarehouse,productSku,productName);
        Map<Long, ProductInfoGetResponse> productInfoVmMap = productInfoGetResponses.parallelStream()
                .collect(Collectors.toMap(ProductInfoGetResponse::id, productInfoGetResponse -> productInfoGetResponse));

        List<Stock> stocks =
                this.stockRepository.findByWarehouseIdAndProductIds(warehouseId,productInfoVmMap.keySet());

        return stocks.stream()
                .map(stock ->  {
                    ProductInfoGetResponse productInfoGetResponse = productInfoVmMap.get(stock.getProductId());
                    return StockGetResponse.fromStock(stock, productInfoGetResponse);
                }).toList();




    }
}
