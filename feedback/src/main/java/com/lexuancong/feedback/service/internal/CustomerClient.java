package com.lexuancong.feedback.service.internal;

import com.lexuancong.feedback.config.ServiceUrlsProperties;
import com.lexuancong.feedback.dto.customer.CustomerResponse;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class CustomerClient {
    private final RestClient restClient;
    private final ServiceUrlsProperties serviceUrlsProperties;
    public CustomerResponse getCustomerInfo(){
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.customer())
                .path("/internal-feedback/customer/profile")
                .buildAndExpand()
                .toUri();

        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .body(CustomerResponse.class);
    }
}
