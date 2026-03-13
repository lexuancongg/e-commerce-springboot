package com.lexuancong.inventory.service;

import com.lexuancong.inventory.constants.Constants;
import com.lexuancong.inventory.model.Stock;
import com.lexuancong.inventory.model.Warehouse;
import com.lexuancong.inventory.repository.StockRepository;
import com.lexuancong.inventory.repository.WarehouseRepository;
import com.lexuancong.inventory.service.Internal.ProductClient;
import com.lexuancong.inventory.dto.product.ProductInfoResponse;
import com.lexuancong.inventory.dto.stock.StockDetailResponse;
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
    private final ProductClient productClient;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseService warehouseService;

    public StockService(StockRepository stockRepository, ProductClient productClient, WarehouseRepository warehouseRepository, WarehouseService warehouseService) {
        this.stockRepository = stockRepository;
        this.productClient = productClient;
        this.warehouseRepository = warehouseRepository;
        this.warehouseService = warehouseService;
    }


    public void addProductIntoWarehouse(List<StockCreateRequest> stockCreateRequestList){
        List<Stock> stocks = new ArrayList<>();
        for (StockCreateRequest stockCreateRequest : stockCreateRequestList) {
            Long productId = stockCreateRequest.productId();

            // cần update sau vì for lớn gây ra performan kém
            ProductInfoResponse productInfo = this.productClient.getProductById(productId);
            if(productInfo == null){
                throw new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, productId);
            }
            Warehouse warehouse = this.warehouseRepository.findById(stockCreateRequest.warehouseId())
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND, stockCreateRequest.warehouseId()));
            this.validateExitedProductInStock(productId, stockCreateRequest.warehouseId());

            Stock stock = Stock.builder()
                    .productId(productId)
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


    public List<StockDetailResponse> getStockByWarehouseIdAndProductSkuAndProductName(Long warehouseId, String productSku, String productName){
        List<Long> productIdsInWarehouse = this.warehouseService.getProductIdsInWarehouse(warehouseId);
        List<ProductInfoResponse> productInfos =
                this.productClient.filterProductInProductIdsByNameOrSku(productIdsInWarehouse,productSku,productName);
        Map<Long, ProductInfoResponse> mapProductInfo = productInfos.parallelStream()
                .collect(Collectors.toMap(ProductInfoResponse::id, productInfoResponse -> productInfoResponse));

        List<Stock> stocks =
                this.stockRepository.findByWarehouseIdAndProductIds(warehouseId,mapProductInfo.keySet());

        return stocks.stream()
                .map(stock ->  {
                    ProductInfoResponse productInfoResponse = mapProductInfo.get(stock.getProductId());
                    return StockDetailResponse.fromStock(stock, productInfoResponse);
                }).toList();




    }
}
