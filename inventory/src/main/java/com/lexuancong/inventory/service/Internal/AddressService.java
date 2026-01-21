package com.lexuancong.inventory.service.Internal;

import com.lexuancong.inventory.config.ServiceUrlConfig;
import com.lexuancong.inventory.dto.address.AddressCreateRequest;
import com.lexuancong.inventory.dto.address.AddressGetResponse;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;


    public AddressGetResponse createAddress(AddressCreateRequest addressCreateRequest){
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.address())
                .path("/internal/addresses")
                .buildAndExpand()
                .toUri();

        return this.restClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .body(addressCreateRequest)
                .retrieve()
                .body(AddressGetResponse.class);


    }

    public void updateAddress(Long id, AddressCreateRequest addressCreateRequest){
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.address())
                .path("/internal/addresses/{id}")
                .buildAndExpand(id)
                .toUri();
        this.restClient.put()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .body(addressCreateRequest)
                .retrieve()
                .body(Void.class);
    }

    public void deleteAddress(Long id){
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.address())
                .path("/internal/addresses/{id}")
                .buildAndExpand(id)
                .toUri();
        this.restClient.delete()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .retrieve()
                .body(Void.class);
    }
}
