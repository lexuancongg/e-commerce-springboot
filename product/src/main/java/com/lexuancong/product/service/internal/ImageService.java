package com.lexuancong.product.service.internal;

import com.lexuancong.product.config.ServiceUrlConfig;
import com.lexuancong.product.viewmodel.image.ImageVm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;
    public ImageVm getImageById (Long id){
        if(id== null){
            return  new ImageVm(null,"");
        }
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.image())
                .path("/images/{id}").buildAndExpand(id)
                .toUri();
        return this.restClient.get()
                .uri(url)
                .retrieve()
                .body(ImageVm.class);



    }

    public List<ImageVm> getImageByIds (List<Long> ids){
        if(ids.isEmpty()){
            return Collections.emptyList();
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.image())
                .path("/images")
                .queryParam("ids", ids);
        URI uri = builder.build().toUri();
        return  this.restClient.get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ImageVm>>(){});

    }
}
