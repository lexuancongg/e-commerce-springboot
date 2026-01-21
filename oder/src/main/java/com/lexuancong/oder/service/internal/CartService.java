package com.lexuancong.oder.service.internal;

import com.lexuancong.oder.config.ServiceUrlConfig;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.dto.cartItem.CartItemDelete;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;
    public void deleteCartItems(Collection<OrderItem> orderItems){
        String jwt = AuthenticationUtils.extractJwt();
        List<CartItemDelete> cartItemDeletes =  orderItems.stream()
                .map(CartItemDelete::fromOderItem)
                .toList();
        URI url = UriComponentsBuilder.fromHttpUrl(this.serviceUrlConfig.cart())
                .path("/internal-order/cart-items/remove")
                .buildAndExpand()
                .toUri();

        restClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt))
                .body(cartItemDeletes)
                .retrieve();
    }
}
