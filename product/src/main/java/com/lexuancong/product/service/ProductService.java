package com.lexuancong.product.service;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.repository.ProductRepository;
import com.lexuancong.product.viewmodel.product.databinding.BaseProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductVariationPropertiesRequire;
import com.lexuancong.product.viewmodel.product.post.ProductPostVm;
import com.lexuancong.product.viewmodel.product.post.ProductSummaryVm;
import com.lexuancong.product.viewmodel.product.post.ProductVariationPostVm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductSummaryVm createProduct(ProductPostVm productPostVm){
        this.validateProduct(productPostVm);
    }


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
    private void validateExistingProductProperties(BaseProductPropertiesRequire baseProductPropertiesRequire,Product existingProduct){

    }





}
