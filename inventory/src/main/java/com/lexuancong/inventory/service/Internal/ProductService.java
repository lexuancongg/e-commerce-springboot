package com.lexuancong.inventory.service.Internal;

import com.lexuancong.inventory.config.ServiceUrlConfig;
import com.lexuancong.inventory.viewmodel.product.ProductInfoVm;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    public ProductInfoVm getProductById(Long productId){
        String jwt  = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.product())
                .path("/internal/products/"+ productId)
                .build()
                .toUri();
        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .body(ProductInfoVm.class);
    }
}
