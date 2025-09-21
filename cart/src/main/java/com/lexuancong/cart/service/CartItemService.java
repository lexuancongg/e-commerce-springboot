package com.lexuancong.cart.service;

import com.lexuancong.cart.constants.Constants;
import com.lexuancong.cart.mapper.CartItemMapper;
import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.repository.CartItemRepository;
import com.lexuancong.cart.service.internal.ProductService;
import com.lexuancong.cart.viewmodel.cartitem.*;
import com.lexuancong.cart.viewmodel.product.ProductPreviewVm;
import com.lexuancong.cart.viewmodel.productoption.ProductOptionValueGetVm;
import com.lexuancong.cart.viewmodel.productoption.ProductOptionValueVm;
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
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;
    public CartItemGetVm addCartItem(CartItemPostVm cartItemPostVm){
        this.validateProduct(cartItemPostVm.productId());
        CartItem cartItem = this.performAddCartItem(cartItemPostVm);
        return CartItemGetVm.fromModel(cartItem);

    }
    private void validateProduct(Long productId){
        if(!productService.checkExistById(productId)){
            throw new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, productId);
        }
    }

    public CartItem performAddCartItem(CartItemPostVm cartItemPostVm){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        try{
            return cartItemRepository.findByCustomerIdAndProductId(customerId,cartItemPostVm.productId())
                    .map(existingCartItem -> updateForCaseExistingCartItem(cartItemPostVm,existingCartItem))
                    .orElseGet(()-> createNewCartItem(cartItemPostVm,customerId));

        }catch (PessimisticLockingFailureException e){

            return null;
        }

    }
    private CartItem updateForCaseExistingCartItem(CartItemPostVm cartItemPostVm, CartItem existingCartItem){
        existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemPostVm.quantity());
        return cartItemRepository.save(existingCartItem);
    }
    private CartItem createNewCartItem(CartItemPostVm cartItemPostVm,String customerId){
        CartItem cartItemEntity =  cartItemPostVm.toModel(customerId);
        return cartItemRepository.save(cartItemEntity);

    }




    // api put cartItem about quantity
    public CartItemGetVm updateCartItem(Long productId, CartItemPutVm cartItemPutVm){
        this.validateProduct(productId);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        CartItem cartItem =  cartItemPutVm.toModel(customerId,productId);
        CartItem cartItemSaved = cartItemRepository.save(cartItem);
        return CartItemGetVm.fromModel(cartItemSaved);

    }


    // api get cartItem
    public List<CartItemDetailVm> getCartItems(){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<CartItem> cartItems = cartItemRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        Map<Long , CartItem> mapCartItemByProductId = cartItems.stream()
                .collect(Collectors.toMap(CartItem::getProductId, Function.identity()));
        List<Long> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .toList();
        List<ProductPreviewVm> productInCartItems = this.productService.getProductListByIds(productIds);
        Map<Long, ProductPreviewVm>  mapProductPreviewByProductId =  productInCartItems.stream()
                .collect(Collectors.toMap(ProductPreviewVm::id, Function.identity()));

        // fetch tới lấy options-value
        List<ProductOptionValueGetVm> productOptionValueGetVms =
                this.productService.getProductOptionValueBySpecificProductIds(productIds);

        Map<Long ,CartItemDetailVm> mapCartItemDetailByProductId = new HashMap<>();
        for (ProductOptionValueGetVm productOptionValueGetVm : productOptionValueGetVms) {
            if(!mapCartItemDetailByProductId.containsKey(productOptionValueGetVm.productId())){
                mapCartItemDetailByProductId.put(
                        productOptionValueGetVm.productOptionId(),
                        new CartItemDetailVm(
                                productOptionValueGetVm.productId(),
                                mapCartItemByProductId.get(productOptionValueGetVm.productId())
                                        .getQuantity(),
                                productOptionValueGetVm.productName(),
                                mapProductPreviewByProductId.get(productOptionValueGetVm.productId())
                                        .slug(),
                                mapProductPreviewByProductId.get(productOptionValueGetVm.productId())
                                        .avatarUrl(),
                                mapProductPreviewByProductId.get(productOptionValueGetVm.productId())
                                        .price(),
                                new ArrayList<>()

                        )

                        );
                continue;
            }
            CartItemDetailVm cartItemDetailVm = mapCartItemDetailByProductId.get(productOptionValueGetVm.productId());
            cartItemDetailVm.productOptionValue().add(
                    new ProductOptionValueVm(
                            productOptionValueGetVm.id(),
                            productOptionValueGetVm.productOptionName(),
                            productOptionValueGetVm.value()
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


    public List<CartItemGetVm> updateCartItemAfterOrder(List<CartItemDeleteVm> cartItemDeleteVms){
        this.validateDuplicatedProductIdInCartItemDelete(cartItemDeleteVms);

        List<Long> productIds = cartItemDeleteVms.stream()
                .map(CartItemDeleteVm::productId)
                .toList();
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<CartItem> cartItems = this.cartItemRepository.findByCustomerIdAndProductIdIn(customerId,productIds);
        Map<Long,CartItem> mapCartItemByProductId = cartItems.stream()
                .collect(Collectors.toMap(CartItem::getProductId, Function.identity()));
        List<CartItem> cartItemsToDelete = new ArrayList<>();
        List<CartItem> cartItemsToUpdateQuantity = new ArrayList<>();
        for (CartItemDeleteVm cartItemDeleteVm : cartItemDeleteVms){
            Optional<CartItem> optionalCartItem=
                    Optional.ofNullable(mapCartItemByProductId.get(cartItemDeleteVm.productId()));

            optionalCartItem.ifPresent(cartItem -> {
                if(cartItem.getQuantity() <= cartItemDeleteVm.quantity()){
                    cartItemsToDelete.add(cartItem);
                }else{
                    cartItem.setQuantity(cartItem.getQuantity() - cartItemDeleteVm.quantity());
                    cartItemsToUpdateQuantity.add(cartItem);
                }
            });
        }

        this.cartItemRepository.deleteAll(cartItemsToDelete);
        this.cartItemRepository.saveAll(cartItemsToUpdateQuantity);
        return cartItemsToUpdateQuantity.stream()
                .map(CartItemGetVm::fromModel)
                .toList();

    }


    private void  validateDuplicatedProductIdInCartItemDelete(List<CartItemDeleteVm> cartItemDeleteVms){
        Map<Long,Integer> mapProductIdToQuantity = new HashMap<>();
        for (CartItemDeleteVm cartItemDeleteVm : cartItemDeleteVms){
            Integer quantityProduct =  mapProductIdToQuantity.get(cartItemDeleteVm.productId());
            if(!Objects.isNull(quantityProduct) && !quantityProduct.equals(cartItemDeleteVm.quantity())){
                throw  new BadRequestException(Constants.ErrorKey.DUPLICATED_CART_ITEMS_TO_DELETE);
            }
            mapProductIdToQuantity.put(cartItemDeleteVm.productId(), cartItemDeleteVm.quantity());
        }
    }
}
