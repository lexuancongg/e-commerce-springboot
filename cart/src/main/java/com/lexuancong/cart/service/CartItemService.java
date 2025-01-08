package com.lexuancong.cart.service;

import com.lexuancong.cart.mapper.CartItemMapper;
import com.lexuancong.cart.repository.CartItemRepository;
import com.lexuancong.cart.utils.AuthenticationUtils;
import com.lexuancong.cart.viewmodel.CartItemGetVm;
import com.lexuancong.cart.viewmodel.CartItemPostVm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;
    public CartItemGetVm addCartItem(CartItemPostVm cartItemPostVm){
        this.validateproduct(cartItemPostVm.productId());
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();


    }
    private void validateproduct(Long productId){
        if(!productService.checkExistById(productId)){
            // ban ra ngoai le
        }

    }
}
