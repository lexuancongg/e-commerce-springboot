package com.lexuancong.feedback.service.internal;

import com.lexuancong.feedback.config.ServiceUrlsProperties;
import com.lexuancong.feedback.dto.order.UserHasBoughtProductCompletedResponse;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class OrderClient {
    private final ServiceUrlsProperties serviceUrlsProperties;
    private final RestClient restClient;
    public UserHasBoughtProductCompletedResponse checkUserHasBoughtProductCompleted(Long productId){
        final String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder
                .fromHttpUrl(this.serviceUrlsProperties.order())
                .path("/customer/order/completed")
                .queryParam("productId", productId)
                .build().toUri();
        return restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .body(UserHasBoughtProductCompletedResponse.class);


    }
}
