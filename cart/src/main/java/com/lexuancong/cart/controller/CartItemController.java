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
    public ResponseEntity<CartItemResponse> addCartItem(@Valid @RequestBody CartItemCreateRequest cartItemCreateRequest){
        CartItemResponse cartItemResponse = cartItemService.addCartItem(cartItemCreateRequest);
        return ResponseEntity.ok(cartItemResponse);
    }

    @PutMapping("/customer/cart-items/{productId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long productId,
                                                           @Valid @RequestBody CartItemUpdateRequest cartItemUpdateRequest){
        CartItemResponse cartItemResponse =
                cartItemService.updateCartItem(productId, cartItemUpdateRequest);
        return  ResponseEntity.ok(cartItemResponse);
    }


    @GetMapping("/customer/cart-items")
    public ResponseEntity<List<CartItemDetailResponse>> getCartItems(){
        List<CartItemDetailResponse> cartItemDetails = cartItemService.getCartItems();
        return ResponseEntity.ok(cartItemDetails);
    }


    @DeleteMapping("/customer/cart-items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId){
        cartItemService.deleteCartItem(productId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/internal-order/cart-items/remove")
    public ResponseEntity<List<CartItemResponse>> removeCartItems(@RequestBody @Valid List<CartItemDeleteRequest> cartItemDeleteRequests){
        return ResponseEntity.ok(this.cartItemService.updateCartItemAfterOrder(cartItemDeleteRequests));

    }

}
