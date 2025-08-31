package com.lexuancong.feedback.service.internal;

import com.lexuancong.feedback.config.ServiceUrlConfig;
import com.lexuancong.feedback.viewmodel.order.CheckUserHasBoughtProductCompletedVm;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ServiceUrlConfig serviceUrlConfig;
    private final RestClient restClient;
    public CheckUserHasBoughtProductCompletedVm checkUserHasBoughtProductCompleted(Long productId){
        final String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder
                .fromHttpUrl(this.serviceUrlConfig.order())
                .path("/customer/order/completed")
                .queryParam("productId", productId)
                .build().toUri();
        return restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .body(CheckUserHasBoughtProductCompletedVm.class);


    }
}
