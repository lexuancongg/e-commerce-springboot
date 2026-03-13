package com.lexuancong.inventory.service.Internal;

import com.lexuancong.inventory.config.ServiceUrlsProperties;
import com.lexuancong.inventory.dto.product.ProductInfoResponse;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductClient {
    private final RestClient restClient;
    private final ServiceUrlsProperties serviceUrlsProperties;

    public ProductInfoResponse getProductById(Long productId){
        String jwt  = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.product())
                .path("/internal/products/"+ productId)
                .build()
                .toUri();
        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .body(ProductInfoResponse.class);
    }

    public List<ProductInfoResponse> filterProductInProductIdsByNameOrSku(List<Long> productIds, String name, String sku){
        String jwt  = AuthenticationUtils.extractJwt();
        if(productIds.isEmpty()){
            return Collections.emptyList();
        }
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("name",name);
        queryParams.add("sku",sku);
        queryParams.add("productIds",productIds.stream().map(Object::toString).collect(Collectors.joining(",")));
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.product())
                .path("/internal/products/warehouse")
                .queryParams(queryParams)
                .build()
                .toUri();
        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductInfoResponse>>() {
                })
                .getBody();
    }
}
