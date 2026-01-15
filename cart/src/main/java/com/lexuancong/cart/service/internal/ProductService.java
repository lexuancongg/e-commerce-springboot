package com.lexuancong.cart.service.internal;

import com.lexuancong.cart.config.ServiceUrlConfig;
import com.lexuancong.cart.dto.product.ProductPreviewGetResponse;
import com.lexuancong.cart.dto.productoption.ProductOptionValueDetailGetResponse;
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
public class ProductService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    public boolean checkExistById(Long productId){
        return this.getProductById(productId) != null;
    }

    private ProductPreviewGetResponse getProductById(Long id){
        List<ProductPreviewGetResponse> productPreviewGetResponse = this.getProductListByIds(List.of(id));
        if(CollectionUtils.isEmpty(productPreviewGetResponse)){
            return null;
        }
        return productPreviewGetResponse.getFirst();
    }


    public List<ProductPreviewGetResponse> getProductListByIds(List<Long> ids){
        final URI requestUrl = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.product())
                .path("/customer/")
                .queryParam("productIds", ids)
                .build()
                .toUri();

        return this.restClient.get()
                .uri(requestUrl)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductPreviewGetResponse>>() {})
                .getBody();
    }

    public List<ProductOptionValueDetailGetResponse> getProductOptionValueBySpecificProductIds(List<Long> productIds){
        String jwt  = AuthenticationUtils.extractJwt();
        URI url =UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.product())
                .path("/customer...")
                .queryParam("productIds",productIds)
                .build()
                .toUri();
        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductOptionValueDetailGetResponse>>() {})
                .getBody();

    }




}
