package com.lexuancong.cart.controller;

import com.lexuancong.cart.constants.Constants;
import com.lexuancong.cart.service.CartItemService;
import com.lexuancong.cart.viewmodel.cartitem.CartItemDeleteVm;
import com.lexuancong.cart.viewmodel.cartitem.CartItemGetVm;
import com.lexuancong.cart.viewmodel.cartitem.CartItemPostVm;
import com.lexuancong.cart.viewmodel.cartitem.CartItemPutVm;
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
    public ResponseEntity<CartItemGetVm> addCartItem(@Valid @RequestBody CartItemPostVm cartItemPostVm){
        CartItemGetVm cartItemGetVm = cartItemService.addCartItem(cartItemPostVm);
        return ResponseEntity.ok(cartItemGetVm);
    }

    @PutMapping("/customer/cart-items/{productId}")
    public ResponseEntity<CartItemGetVm> updateCartItem(@PathVariable Long productId,
                                                        @Valid @RequestBody CartItemPutVm cartItemPutVm){
        CartItemGetVm cartItemGetVm = cartItemService.updateCartItem(productId,cartItemPutVm);
        return  ResponseEntity.ok(cartItemGetVm);
    }


    @GetMapping("/customer/cart-items")
    public ResponseEntity<List<CartItemGetVm>> getCartItems(){
        List<CartItemGetVm> cartItemGetVms = cartItemService.getCartItems();
        return ResponseEntity.ok(cartItemGetVms);
    }

    @DeleteMapping("/customer/cart-items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId){
        cartItemService.deleteCartItem(productId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/internal-order/cart-items/remove")
    public ResponseEntity<List<CartItemGetVm>> removeCartItems(@RequestBody @Valid List<CartItemDeleteVm> cartItemDeleteVms){
        return ResponseEntity.ok(this.cartItemService.updateCartItemAfterOrder(cartItemDeleteVms));

    }

}
