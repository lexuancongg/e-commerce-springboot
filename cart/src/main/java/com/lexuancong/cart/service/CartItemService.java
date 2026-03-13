package com.lexuancong.cart.service;

import com.lexuancong.cart.constants.Constants;
import com.lexuancong.cart.dto.productoption.ProductOptionValueDetailResponse;
import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.repository.CartItemRepository;
import com.lexuancong.cart.service.internal.ProductClient;
import com.lexuancong.cart.dto.cartitem.*;
import com.lexuancong.cart.dto.product.ProductPreviewResponse;
import com.lexuancong.cart.dto.productoption.ProductOptionValueResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    public CartItemResponse addCartItem(CartItemCreateRequest cartItemCreateRequest){
        this.validateProductExist(cartItemCreateRequest.productId());
        CartItem cartItem = this.performAddCartItem(cartItemCreateRequest);
        return CartItemResponse.fromCartItem(cartItem);

    }
    private void validateProductExist(Long productId){
        if(!productClient.checkExistById(productId)){
            throw new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, productId);
        }
    }

    public CartItem performAddCartItem(CartItemCreateRequest cartItemCreateRequest){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        try{
            return cartItemRepository.findByCustomerIdAndProductId(customerId, cartItemCreateRequest.productId())
                    .map(existingCartItem -> updateForCaseExistingCartItem(cartItemCreateRequest,existingCartItem))
                    .orElseGet(()-> createNewCartItem(cartItemCreateRequest,customerId));

        }catch (PessimisticLockingFailureException e){

            return null;
        }

    }
    private CartItem updateForCaseExistingCartItem(CartItemCreateRequest cartItemCreateRequest, CartItem existingCartItem){
        existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemCreateRequest.quantity());
        return cartItemRepository.save(existingCartItem);
    }
    private CartItem createNewCartItem(CartItemCreateRequest cartItemCreateRequest, String customerId){
        CartItem cartItemEntity =  cartItemCreateRequest.toCartItem(customerId);
        return cartItemRepository.save(cartItemEntity);

    }




    public CartItemResponse updateCartItem(Long productId, CartItemUpdateRequest cartItemUpdateRequest){
        this.validateProductExist(productId);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        CartItem cartItem =  cartItemUpdateRequest.toCartItem(customerId,productId);
        CartItem cartItemSaved = cartItemRepository.save(cartItem);
        return CartItemResponse.fromCartItem(cartItemSaved);

    }


    public List<CartItemDetailResponse> getCartItems(){

        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();

        List<CartItem> cartItems = cartItemRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        Map<Long , CartItem> mapCartItemByProductId = cartItems.stream()
                .collect(
                        Collectors.toMap(CartItem::getProductId, Function.identity())
                );
        List<Long> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .toList();
        List<ProductPreviewResponse> productInCartItems = this.productClient.getProductListByIds(productIds);

        Map<Long, ProductPreviewResponse>  mapProductPreviewByProductId =  productInCartItems.stream()
                .collect(Collectors.toMap(ProductPreviewResponse::id, Function.identity()));

        // fetch tới lấy options-value
        List<ProductOptionValueDetailResponse> productOptionValueDetailResponses =
                this.productClient.getProductOptionValueBySpecificProductIds(productIds);

        Map<Long , CartItemDetailResponse> mapCartItemDetailByProductId = new HashMap<>();

        for (ProductOptionValueDetailResponse productOptionValueDetailResponse : productOptionValueDetailResponses) {
            ProductOptionValueResponse optionValue = new ProductOptionValueResponse(
                    productOptionValueDetailResponse.id(),
                    productOptionValueDetailResponse.optionName(),
                    productOptionValueDetailResponse.value()

            );


            if(!mapCartItemDetailByProductId.containsKey(productOptionValueDetailResponse.productId())){
                ProductPreviewResponse productPreview  =
                        mapProductPreviewByProductId.get(productOptionValueDetailResponse.productId());


                mapCartItemDetailByProductId.put(
                        productOptionValueDetailResponse.productId(),
                        CartItemDetailResponse.builder()
                                .productId(productPreview.id())
                                .avatarUrl(productPreview.avatarUrl())
                                .price(productPreview.price())
                                .slug(productPreview.slug())
                                .productName(productPreview.name())
                                .quantity(mapCartItemByProductId.get(productOptionValueDetailResponse.productId()).getQuantity())
                                .productOptionValues(new ArrayList<>(
                                        List.of(optionValue)
                                ))
                                .build()
                        );
                continue;
            }
            CartItemDetailResponse cartItemDetail =
                    mapCartItemDetailByProductId.get(productOptionValueDetailResponse.productId());
            cartItemDetail.productOptionValues().add(optionValue);
        }

        return new ArrayList<>(mapCartItemDetailByProductId.values());

    }


    public void deleteCartItem(Long productId){
        this.validateProductExist(productId);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        cartItemRepository.deleteByCustomerIdAndProductId(customerId,productId);
    }




}
