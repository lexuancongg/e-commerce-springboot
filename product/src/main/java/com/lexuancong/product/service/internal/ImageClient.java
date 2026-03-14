package com.lexuancong.product.service.internal;

import com.lexuancong.product.config.ServiceUrlsProperties;
import com.lexuancong.product.dto.image.ImagePreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageClient {
    private final RestClient restClient;
    private final ServiceUrlsProperties serviceUrlsProperties;
    public ImagePreviewResponse getImageById (Long id){
        if(id== null){
            return  new ImagePreviewResponse(null,"");
        }
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.image())
                .path("/images/{id}").buildAndExpand(id)
                .toUri();
        return this.restClient.get()
                .uri(url)
                .retrieve()
                .body(ImagePreviewResponse.class);



    }

    public List<ImagePreviewResponse> getImageByIds (List<Long> ids){
        if(ids.isEmpty()){
            return Collections.emptyList();
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.serviceUrlsProperties.image())
                .path("/images")
                .queryParam("ids", ids);
        URI uri = builder.build().toUri();
        return  this.restClient.get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ImagePreviewResponse>>(){});

    }
}
