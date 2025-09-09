package com.lexuancong.cart.viewmodel.productoption;

public record ProductOptionValueGetVm(
        Long productId ,
        Long id ,
        String productName,

        Long productOptionId,
        String productOptionName,
        String value
){

}


