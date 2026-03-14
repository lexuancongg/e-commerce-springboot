package com.lexuancong.cart.kafka.consumer;

import com.lexuancong.cart.constants.Constants;
import com.lexuancong.cart.dto.cartitem.CartItemResponse;
import com.lexuancong.cart.dto.cartitem.ProductSubtractQuantity;
import com.lexuancong.cart.kafka.message.OrderCreatedMessage;
import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.repository.CartItemRepository;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.kafka.KafkaTopics;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CartSyncDataConsumer {
    private final CartItemRepository cartItemRepository;

    @KafkaListener(
            topics = KafkaTopics.ORDER_CREATED,
            containerFactory = "orderCreatedListenerFactory"
    )
    public List<CartItemResponse> updateCartItemAfterOrder(OrderCreatedMessage orderCreatedMessage) {
        List<ProductSubtractQuantity> productSubtractQuantities =orderCreatedMessage.productSubtract();
        String customerId = orderCreatedMessage.customerId();
        this.validateDuplicatedProductIdInCartItemDelete(productSubtractQuantities);

        List<Long> productIds = productSubtractQuantities.stream()
                .map(ProductSubtractQuantity::productId)
                .toList();


        List<CartItem> cartItems = this.cartItemRepository.findByCustomerIdAndProductIdIn(customerId,productIds);

        Map<Long,CartItem> mapCartItemByProductId = cartItems.stream()
                .collect(Collectors.toMap(CartItem::getProductId, Function.identity()));
        List<CartItem> cartItemsToDelete = new ArrayList<>();
        List<CartItem> cartItemsToUpdateQuantity = new ArrayList<>();
        for (ProductSubtractQuantity productSubtractQuantity : productSubtractQuantities){
            Optional<CartItem> optionalCartItem=
                    Optional.ofNullable(mapCartItemByProductId.get(productSubtractQuantity.productId()));

            optionalCartItem.ifPresent(cartItem -> {
                if(cartItem.getQuantity() <= productSubtractQuantity.quantity()){
                    cartItemsToDelete.add(cartItem);
                }else{
                    cartItem.setQuantity(cartItem.getQuantity() - productSubtractQuantity.quantity());
                    cartItemsToUpdateQuantity.add(cartItem);
                }
            });
        }

        this.cartItemRepository.deleteAll(cartItemsToDelete);
        this.cartItemRepository.saveAll(cartItemsToUpdateQuantity);
        return cartItemsToUpdateQuantity.stream()
                .map(CartItemResponse::fromCartItem)
                .toList();

    }



    private void  validateDuplicatedProductIdInCartItemDelete(List<ProductSubtractQuantity> requests){
        Map<Long,Integer> map = new HashMap<>();
        for (ProductSubtractQuantity req : requests){
            Integer quantityProduct =  map.putIfAbsent(req.productId(),req.quantity());

            if(!Objects.isNull(quantityProduct) && !quantityProduct.equals(req.quantity())){
                throw  new BadRequestException(Constants.ErrorKey.DUPLICATED_CART_ITEMS_TO_DELETE);
            }
        }
    }
}
