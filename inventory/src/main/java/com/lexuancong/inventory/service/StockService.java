package com.lexuancong.inventory.service;

import com.lexuancong.inventory.model.Stock;
import com.lexuancong.inventory.repository.StockRepository;
import com.lexuancong.inventory.service.Internal.ProductService;
import com.lexuancong.inventory.viewmodel.product.ProductInfoVm;
import com.lexuancong.inventory.viewmodel.stock.StockPostVm;
import com.lexuancong.inventory.viewmodel.stock.StockPutQuantityVm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final ProductService productService;

    public StockService(StockRepository stockRepository, ProductService productService) {
        this.stockRepository = stockRepository;
        this.productService = productService;
    }


    public void addProductInStock(List<StockPostVm> stockPostVmList){
        List<Stock> stocks = new ArrayList<>();
        for (StockPostVm stockPostVm : stockPostVmList) {

            // check sp xem
            ProductInfoVm productInfoVm = this.productService.getProductById(stockPostVm.productId());
            if(productInfoVm == null){
                throw new RuntimeException();
            }


            boolean isExistedProductInStock = this.stockRepository.existsByProductId(stockPostVm.productId());
            if(isExistedProductInStock){
                throw new RuntimeException();
            }
            Stock stock = Stock.builder()
                    .productId(stockPostVm.productId())
                    .quantity(0)
                    .lockedQuantity(0)
                    .build();
            stocks.add(stock);

        }
        this.stockRepository.saveAll(stocks);
    }


    public void updateQuantityProductInStock(List<StockPutQuantityVm> stockPutQuantityVms){
        List<Long> productIds = stockPutQuantityVms.parallelStream()
                .map(StockPutQuantityVm::productId)
                .toList();

        List<Stock> stocks = this.stockRepository.findAllByProductIdIn(productIds);
        Map<Long,Stock> mapStockKeyProductId = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductId, stock -> stock));

        for (StockPutQuantityVm stockPutQuantityVm : stockPutQuantityVms) {
            Long productId = stockPutQuantityVm.productId();
            Stock stock = mapStockKeyProductId.get(productId);
            if(stock == null)
                continue;
            stock.setQuantity(stockPutQuantityVm.quantity());
        }
        // tham chiếu nên stocks được cập nhật
        this.stockRepository.saveAll(stocks);
    }
}
