package com.lexuancong.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final RestClient restClient;

    public boolean checkExistById(Long productId){
        // demo sau
        return true;
    }




}
