package com.lexuancong.search.service;

import com.lexuancong.search.model.Product;
import com.lexuancong.search.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSyncDataService {
    private final ProductRepository productRepository;
    public void createProductSyncData(Long productId , Product newProduct) {
        // cách 1 => dùng id gọi http tới service product lấy thông tin => mapper vào rồi lưu
        this.productRepository.save(newProduct);

    }
    public void deleteProductSyncData(Long productId) {
        boolean isExist = this.productRepository.existsById(productId);
        if (isExist) {
            this.productRepository.deleteById(productId);
        }
    }
    public void updateProductSyncData(Long productId , Product newProduct) {
       Product oldProduct = this.productRepository.findById(productId)
               .orElseThrow(() -> new RuntimeException("not found"));
       oldProduct.setName(newProduct.getName());
       oldProduct.setSlug(newProduct.getSlug());
       oldProduct.setPrice(newProduct.getPrice());
       oldProduct.setBrand(newProduct.getBrand());
       oldProduct.setAvatarImageId(newProduct.getAvatarImageId());
       this.productRepository.save(oldProduct);
    }

}
