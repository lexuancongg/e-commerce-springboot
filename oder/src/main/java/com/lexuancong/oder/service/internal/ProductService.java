package com.lexuancong.oder.service.internal;

import com.lexuancong.oder.config.ServiceUrlConfig;
import com.lexuancong.oder.viewmodel.product.ProductCheckoutPreviewVm;
import com.lexuancong.oder.viewmodel.product.ProductVariantPreviewVm;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ServiceUrlConfig serviceUrlConfig;
    private final RestClient restClient;

    public List<ProductVariantPreviewVm> getProductVariantByProductParentId(Long productId) {
        return null;
    }

    public List<ProductCheckoutPreviewVm>  getProductInfoPreviewByIds(Collection<Long> productIds) {
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.product())
                .path("/internal-order/products")
                .queryParam("ids",productIds)
                .buildAndExpand()
                .toUri();

        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductCheckoutPreviewVm>>() {
                })
                .getBody();

    }

}
