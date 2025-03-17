package com.lexuancong.product.service;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.viewmodel.product.databinding.BaseProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductVariationPropertiesRequire;
import com.lexuancong.product.viewmodel.product.post.ProductPostVm;
import com.lexuancong.product.viewmodel.product.post.ProductSummaryVm;
import com.lexuancong.product.viewmodel.product.post.ProductVariationPostVm;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductSummaryVm createProduct(ProductPostVm productPostVm){
        this.validateProduct(productPostVm);
    }


    // dành cho create
    private <T extends ProductVariationPropertiesRequire > void validateProduct(ProductPropertiesRequire<T> productVmToSave){
        this.validateProduct(productVmToSave,null);
    }


    // gộp cho cả trường hợp create và update
    private <T extends ProductVariationPropertiesRequire> void validateProduct(ProductPropertiesRequire<T> productVmToSave, Product existingProduct){
        this.validateLengthMustGreaterThanWidth(productVmToSave);
        this.validateExistingProductProperties(productVmToSave,existingProduct);




    }
    // check xem chieeuf dài và chiều rộng đươc nhập hhowpjp lệ không => tính toán tiền vận chuyển sau nay
    private <T extends ProductVariationPropertiesRequire> void validateLengthMustGreaterThanWidth(ProductPropertiesRequire<T> productVmToSave){
        if(productVmToSave.length() < productVmToSave.width()) {
            // bắn ra ngoại lệ
        }
    }

    // check xem các thuộc tinhs của sp có bị trùng lặp khoong
    private void validateExistingProductProperties(BaseProductPropertiesRequire baseProductProperties,Product existingProduct){
        this.checkPropertyExisted(baseProductProperties.slug().toLowerCase(),this.productRepository::findBySlug,existingProduct);
        if (StringUtils.isNotEmpty(baseProductProperties.gtin())){
            this.checkPropertyExisted(baseProductProperties.gtin(),this.productRepository::findByGtin,existingProduct);
        }
        this.checkPropertyExisted(baseProductProperties.sku(),this.productRepository::findBySku,existingProduct);
    }

    // cách thức check các thuộc tính tuương tự nhau (cần tên thuộc tính , đưa method từ jpa) => hàm
    private void checkPropertyExisted(String propertyValue, Function<String, Optional<Product>> finder ,Product existingProduct){
        finder.apply(propertyValue).ifPresent(product -> {
            if(existingProduct == null || !product.getId().equals(existingProduct.getId()) ){
                // throw exception
            }


        });
    }





}
