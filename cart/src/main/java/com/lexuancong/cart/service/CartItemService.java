package com.lexuancong.cart.service;

import com.lexuancong.cart.constants.Constants;
import com.lexuancong.cart.dto.productoption.ProductOptionValueDetailGetResponse;
import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.repository.CartItemRepository;
import com.lexuancong.cart.service.internal.ProductService;
import com.lexuancong.cart.dto.cartitem.*;
import com.lexuancong.cart.dto.product.ProductPreviewGetResponse;
import com.lexuancong.cart.dto.productoption.ProductOptionValueGetResponse;
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
    private final ProductService productService;
    public CartItemGetResponse addCartItem(CartItemCreateRequest cartItemCreateRequest){
        this.validateProduct(cartItemCreateRequest.productId());
        CartItem cartItem = this.performAddCartItem(cartItemCreateRequest);
        return CartItemGetResponse.fromCartItem(cartItem);

    }
    private void validateProduct(Long productId){
        if(!productService.checkExistById(productId)){
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




    // api put cartItem about quantity
    public CartItemGetResponse updateCartItem(Long productId, CartItemUpdateRequest cartItemUpdateRequest){
        this.validateProduct(productId);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        CartItem cartItem =  cartItemUpdateRequest.toCartItem(customerId,productId);
        CartItem cartItemSaved = cartItemRepository.save(cartItem);
        return CartItemGetResponse.fromCartItem(cartItemSaved);

    }


    public List<CartItemDetailGetResponse> getCartItems(){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<CartItem> cartItems = cartItemRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        Map<Long , CartItem> mapCartItemByProductId = cartItems.stream()
                .collect(Collectors.toMap(CartItem::getProductId, Function.identity()));
        List<Long> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .toList();
        List<ProductPreviewGetResponse> productInCartItems = this.productService.getProductListByIds(productIds);
        Map<Long, ProductPreviewGetResponse>  mapProductPreviewByProductId =  productInCartItems.stream()
                .collect(Collectors.toMap(ProductPreviewGetResponse::id, Function.identity()));

        // fetch tới lấy options-value
        List<ProductOptionValueDetailGetResponse> productOptionValueDetailResponses =
                this.productService.getProductOptionValueBySpecificProductIds(productIds);

        Map<Long , CartItemDetailGetResponse> mapCartItemDetailByProductId = new HashMap<>();
        for (ProductOptionValueDetailGetResponse productOptionValueDetailGetResponse : productOptionValueDetailResponses) {
            if(!mapCartItemDetailByProductId.containsKey(productOptionValueDetailGetResponse.productId())){
                mapCartItemDetailByProductId.put(
                        productOptionValueDetailGetResponse.productOptionId(),
                        new CartItemDetailGetResponse(
                                productOptionValueDetailGetResponse.productId(),
                                mapCartItemByProductId.get(productOptionValueDetailGetResponse.productId())
                                        .getQuantity(),
                                productOptionValueDetailGetResponse.productName(),
                                mapProductPreviewByProductId.get(productOptionValueDetailGetResponse.productId())
                                        .slug(),
                                mapProductPreviewByProductId.get(productOptionValueDetailGetResponse.productId())
                                        .avatarUrl(),
                                mapProductPreviewByProductId.get(productOptionValueDetailGetResponse.productId())
                                        .price(),
                                new ArrayList<>()

                        )

                        );
                continue;
            }
            CartItemDetailGetResponse cartItemDetailGetResponse = mapCartItemDetailByProductId.get(productOptionValueDetailGetResponse.productId());
            cartItemDetailGetResponse.productOptionValue().add(
                    new ProductOptionValueGetResponse(
                            productOptionValueDetailGetResponse.id(),
                            productOptionValueDetailGetResponse.productOptionName(),
                            productOptionValueDetailGetResponse.value()
                    )
            );
        }

        return new ArrayList<>(mapCartItemDetailByProductId.values());

    }


    // api delete
    public void deleteCartItem(Long productId){
        this.validateProduct(productId);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        cartItemRepository.deleteByCustomerIdAndProductId(customerId,productId);
    }


    public List<CartItemGetResponse> updateCartItemAfterOrder(List<CartItemDeletRequest> cartItemDeletRequests){
        this.validateDuplicatedProductIdInCartItemDelete(cartItemDeletRequests);

        List<Long> productIds = cartItemDeletRequests.stream()
                .map(CartItemDeletRequest::productId)
                .toList();
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<CartItem> cartItems = this.cartItemRepository.findByCustomerIdAndProductIdIn(customerId,productIds);
        Map<Long,CartItem> mapCartItemByProductId = cartItems.stream()
                .collect(Collectors.toMap(CartItem::getProductId, Function.identity()));
        List<CartItem> cartItemsToDelete = new ArrayList<>();
        List<CartItem> cartItemsToUpdateQuantity = new ArrayList<>();
        for (CartItemDeletRequest cartItemDeletRequest : cartItemDeletRequests){
            Optional<CartItem> optionalCartItem=
                    Optional.ofNullable(mapCartItemByProductId.get(cartItemDeletRequest.productId()));

            optionalCartItem.ifPresent(cartItem -> {
                if(cartItem.getQuantity() <= cartItemDeletRequest.quantity()){
                    cartItemsToDelete.add(cartItem);
                }else{
                    cartItem.setQuantity(cartItem.getQuantity() - cartItemDeletRequest.quantity());
                    cartItemsToUpdateQuantity.add(cartItem);
                }
            });
        }

        this.cartItemRepository.deleteAll(cartItemsToDelete);
        this.cartItemRepository.saveAll(cartItemsToUpdateQuantity);
        return cartItemsToUpdateQuantity.stream()
                .map(CartItemGetResponse::fromCartItem)
                .toList();

    }


    private void  validateDuplicatedProductIdInCartItemDelete(List<CartItemDeletRequest> cartItemDeletRequests){
        Map<Long,Integer> mapProductIdToQuantity = new HashMap<>();
        for (CartItemDeletRequest cartItemDeletRequest : cartItemDeletRequests){
            Integer quantityProduct =  mapProductIdToQuantity.get(cartItemDeletRequest.productId());
            if(!Objects.isNull(quantityProduct) && !quantityProduct.equals(cartItemDeletRequest.quantity())){
                throw  new BadRequestException(Constants.ErrorKey.DUPLICATED_CART_ITEMS_TO_DELETE);
            }
            mapProductIdToQuantity.put(cartItemDeletRequest.productId(), cartItemDeletRequest.quantity());
        }
    }
}
