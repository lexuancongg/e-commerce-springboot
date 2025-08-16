package com.lexuancong.cart.service.internal;

import com.lexuancong.cart.config.ServiceUrlConfig;
import com.lexuancong.cart.viewmodel.product.ProductPreviewVm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    public boolean checkExistById(Long productId){
        return this.getProductById(productId) != null;
    }

    private ProductPreviewVm getProductById(Long id){
        List<ProductPreviewVm> productPreviewVm = this.getProductListByIds(List.of(id));
        if(CollectionUtils.isEmpty(productPreviewVm)){
            return null;
        }
        return productPreviewVm.getFirst();
    }


    private List<ProductPreviewVm> getProductListByIds(List<Long> ids){
        final URI requestUrl = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.product())
                .path("/customer/")
                .queryParam("productIds", ids)
                .build()
                .toUri();

        return this.restClient.get()
                .uri(requestUrl)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductPreviewVm>>() {})
                .getBody();
    }




}
