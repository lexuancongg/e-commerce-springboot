package com.lexuancong.inventory.service.Internal;

import com.lexuancong.inventory.config.ServiceUrlsProperties;
import com.lexuancong.inventory.dto.address.AddressCreateRequest;
import com.lexuancong.inventory.dto.address.AddressResponse;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class AddressClient {
    private final RestClient restClient;
    private final ServiceUrlsProperties serviceUrlsProperties;


    public AddressResponse createAddress(AddressCreateRequest addressCreateRequest){
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.address())
                .path("/internal-inventory/addresses")
                .buildAndExpand()
                .toUri();

        return this.restClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .body(addressCreateRequest)
                .retrieve()
                .body(AddressResponse.class);


    }

    public void updateAddress(Long id, AddressCreateRequest addressCreateRequest){
        String jwt = AuthenticationUtils.extractJwt();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.address())
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
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.address())
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
