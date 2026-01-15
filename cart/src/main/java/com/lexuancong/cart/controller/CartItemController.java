package com.lexuancong.cart.controller;

import com.lexuancong.cart.constants.Constants;
import com.lexuancong.cart.service.CartItemService;
import com.lexuancong.cart.dto.cartitem.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.ApiConstants.CART_BASE_URL)
public class CartItemController {
    private final CartItemService cartItemService;
    @PostMapping("customer/cart-items")
    public ResponseEntity<CartItemGetResponse> addCartItem(@Valid @RequestBody CartItemCreateRequest cartItemCreateRequest){
        CartItemGetResponse cartItemGetResponse = cartItemService.addCartItem(cartItemCreateRequest);
        return ResponseEntity.ok(cartItemGetResponse);
    }

    @PutMapping("/customer/cart-items/{productId}")
    public ResponseEntity<CartItemGetResponse> updateCartItem(@PathVariable Long productId,
                                                              @Valid @RequestBody CartItemUpdateRequest cartItemUpdateRequest){
        CartItemGetResponse cartItemGetResponse = cartItemService.updateCartItem(productId, cartItemUpdateRequest);
        return  ResponseEntity.ok(cartItemGetResponse);
    }


    @GetMapping("/customer/cart-items")
    public ResponseEntity<List<CartItemDetailGetResponse>> getCartItems(){
        List<CartItemDetailGetResponse> cartItemGetVms = cartItemService.getCartItems();
        return ResponseEntity.ok(cartItemGetVms);
    }


    @DeleteMapping("/customer/cart-items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId){
        cartItemService.deleteCartItem(productId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/internal-order/cart-items/remove")
    public ResponseEntity<List<CartItemGetResponse>> removeCartItems(@RequestBody @Valid List<CartItemDeletRequest> cartItemDeletRequests){
        return ResponseEntity.ok(this.cartItemService.updateCartItemAfterOrder(cartItemDeletRequests));

    }

}
