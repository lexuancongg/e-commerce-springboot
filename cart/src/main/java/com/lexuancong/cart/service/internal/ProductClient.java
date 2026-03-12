package com.lexuancong.cart.service.internal;

import com.lexuancong.cart.config.ServiceUrlsProperties;
import com.lexuancong.cart.dto.product.ProductPreviewResponse;
import com.lexuancong.cart.dto.productoption.ProductOptionValueDetailResponse;
import com.lexuancong.share.utils.AuthenticationUtils;
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
public class ProductClient {
    private final RestClient restClient;
    private final ServiceUrlsProperties serviceUrlsProperties;

    public boolean checkExistById(Long productId){
        return this.getProductById(productId) != null;
    }

    private ProductPreviewResponse getProductById(Long id){
        List<ProductPreviewResponse> productPreviewResponse = this.getProductListByIds(List.of(id));
        if(CollectionUtils.isEmpty(productPreviewResponse)){
            return null;
        }
        return productPreviewResponse.getFirst();
    }


    public List<ProductPreviewResponse> getProductListByIds(List<Long> ids){
        final URI requestUrl = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.product())
                .path("/customer/")
                .queryParam("productIds", ids)
                .build()
                .toUri();

        return this.restClient.get()
                .uri(requestUrl)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductPreviewResponse>>() {})
                .getBody();
    }

    public List<ProductOptionValueDetailResponse> getProductOptionValueBySpecificProductIds(List<Long> productIds){
        String jwt  = AuthenticationUtils.extractJwt();
        URI url =UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.product())
                .path("/customer...")
                .queryParam("productIds",productIds)
                .build()
                .toUri();
        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductOptionValueDetailResponse>>() {})
                .getBody();

    }




}
