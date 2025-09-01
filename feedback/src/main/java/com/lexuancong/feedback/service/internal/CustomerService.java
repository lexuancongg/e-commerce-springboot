package com.lexuancong.feedback.service.internal;

import com.lexuancong.feedback.config.ServiceUrlConfig;
import com.lexuancong.feedback.viewmodel.customer.CustomerVm;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;
    public CustomerVm getCustomerInfo(){
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.customer())
                .path("/internal/customer/profile")
                .buildAndExpand()
                .toUri();

        return this.restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .body(CustomerVm.class);
    }
}
