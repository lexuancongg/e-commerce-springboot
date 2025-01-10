package com.lexuancong.cart.controller;

import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.service.CartItemService;
import com.lexuancong.cart.viewmodel.CartItemGetVm;
import com.lexuancong.cart.viewmodel.CartItemPostVm;
import com.lexuancong.cart.viewmodel.CartItemPutVm;
import jakarta.validation.Valid;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// tự động tạo constructor cho các trường final
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;
    @PostMapping("storefront/cart/items")
    public ResponseEntity<CartItemGetVm> addCartItem(@Valid @RequestBody CartItemPostVm cartItemPostVm){
        CartItemGetVm cartItemGetVm = cartItemService.addCartItem(cartItemPostVm);
        return ResponseEntity.ok(cartItemGetVm);
    }

    @PutMapping("/storefront/cart/items/{productId}")
    public ResponseEntity<CartItemGetVm> updateCartItem(@PathVariable Long productId,
                                                        @Valid @RequestBody CartItemPutVm cartItemPutVm){
        CartItemGetVm cartItemGetVm = cartItemService.updateCartItem(productId,cartItemPutVm);
        return  ResponseEntity.ok(cartItemGetVm);
    }

    @GetMapping("/storefront/cart/items")
    public ResponseEntity<List<CartItemGetVm>> getCartItems(){
        List<CartItemGetVm> cartItemGetVms = cartItemService.getCartItems();
        return ResponseEntity.ok(cartItemGetVms);
    }

    @DeleteMapping("/storefront/cart/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId){
        cartItemService.deleteCartItem(productId);
        return ResponseEntity.noContent().build();
    }

}
