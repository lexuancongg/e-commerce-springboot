package com.lexuancong.oder.service.internal;

import com.lexuancong.oder.config.ServiceUrlConfig;
import com.lexuancong.oder.viewmodel.product.ProductVariantPreviewVm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ServiceUrlConfig serviceUrlConfig;
    private final RestClient restClient;

    public List<ProductVariantPreviewVm> getProductVariantByProductParentId(Long productId) {


    }

}
