package com.lexuancong.cart.dto.productoption;

public record ProductOptionValueDetailGetResponse(
        Long productId ,
        Long id ,
        String productName,

        Long productOptionId,
        String productOptionName,
        String value
){

}


