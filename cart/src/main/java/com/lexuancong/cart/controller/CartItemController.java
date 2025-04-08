package com.lexuancong.cart.controller;

import com.lexuancong.cart.service.CartItemService;
import com.lexuancong.cart.viewmodel.CartItemVm;
import com.lexuancong.cart.viewmodel.CartItemPostVm;
import com.lexuancong.cart.viewmodel.CartItemPutVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// tự động tạo constructor cho các trường final
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;
    @PostMapping("customer/cart/items")
    public ResponseEntity<CartItemVm> addCartItem(@Valid @RequestBody CartItemPostVm cartItemPostVm){
        CartItemVm cartItemVm = cartItemService.addCartItem(cartItemPostVm);
        return ResponseEntity.ok(cartItemVm);
    }

    @PutMapping("/customer/cart/items/{productId}")
    public ResponseEntity<CartItemVm> updateCartItem(@PathVariable Long productId,
                                                     @Valid @RequestBody CartItemPutVm cartItemPutVm){
        CartItemVm cartItemVm = cartItemService.updateCartItem(productId,cartItemPutVm);
        return  ResponseEntity.ok(cartItemVm);
    }


    @GetMapping("/customer/cart/items")
    public ResponseEntity<List<CartItemVm>> getCartItems(){
        List<CartItemVm> cartItemVms = cartItemService.getCartItems();
        return ResponseEntity.ok(cartItemVms);
    }

    @DeleteMapping("/customer/cart/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId){
        cartItemService.deleteCartItem(productId);
        return ResponseEntity.noContent().build();
    }

}
