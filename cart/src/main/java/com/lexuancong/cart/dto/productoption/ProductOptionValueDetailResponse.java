package com.lexuancong.cart.dto.productoption;

public record ProductOptionValueDetailResponse(
        Long productId ,
        Long id ,
        Long optionId,
        String optionName,
        String value
){

}


