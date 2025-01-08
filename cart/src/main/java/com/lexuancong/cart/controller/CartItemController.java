package com.lexuancong.cart.controller;

import com.lexuancong.cart.service.CartItemService;
import com.lexuancong.cart.viewmodel.CartItemGetVm;
import com.lexuancong.cart.viewmodel.CartItemPostVm;
import jakarta.validation.Valid;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
