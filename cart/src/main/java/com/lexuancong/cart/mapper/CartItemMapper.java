package com.lexuancong.cart.mapper;

import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.viewmodel.cartitem.CartItemGetVm;
import com.lexuancong.cart.viewmodel.cartitem.CartItemPostVm;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {
    // add
    public CartItem toCartItem(CartItemPostVm cartItemPostVm,String customerId){
        return CartItem.builder()
                .customerId(customerId)
                .productId(cartItemPostVm.productId())
                .quantity(cartItemPostVm.quantity())
                .build();
    }
    // return for frontend
    public CartItemGetVm toCartItemGetVm(CartItem cartItem){
        return CartItemGetVm.builder()
                .customerId(cartItem.getCustomerId())
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProductId())
                .build();
    }

    // update
    public CartItem toCartItem(String customerId,Long productId,int quantity){
        return CartItem.builder()
                .customerId(customerId)
                .productId(productId)
                .quantity(quantity)
                .build();
    }

    // convert for list
    public List<CartItemGetVm> toCartItemGetVmList(List<CartItem> cartItemList){
        return cartItemList.stream().map(this::toCartItemGetVm).collect(Collectors.toList());
    }

}

