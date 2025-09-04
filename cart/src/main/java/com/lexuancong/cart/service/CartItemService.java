package com.lexuancong.cart.service;

import com.lexuancong.cart.constants.Constants;
import com.lexuancong.cart.mapper.CartItemMapper;
import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.repository.CartItemRepository;
import com.lexuancong.cart.service.internal.ProductService;
import com.lexuancong.cart.viewmodel.cartitem.CartItemDeleteVm;
import com.lexuancong.cart.viewmodel.cartitem.CartItemGetVm;
import com.lexuancong.cart.viewmodel.cartitem.CartItemPostVm;
import com.lexuancong.cart.viewmodel.cartitem.CartItemPutVm;
import com.lexuancong.share.exception.BadRequestException;
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
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;
    public CartItemGetVm addCartItem(CartItemPostVm cartItemPostVm){
        this.validateProduct(cartItemPostVm.productId());
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        CartItem cartItem = performAddCartItem(cartItemPostVm,customerId);
        return cartItemMapper.toCartItemGetVm(cartItem);

    }
    private void validateProduct(Long productId){
        if(!productService.checkExistById(productId)){


        }
    }

    @Transactional
    public CartItem performAddCartItem(CartItemPostVm cartItemPostVm,String customerId){
        try{
            return cartItemRepository.findByCustomerIdAndProductId(customerId,cartItemPostVm.productId())
                    // map nayf dungf ánh xạ giá trị cua optionl chứa
                    .map(existingCartItem -> updateForCaseExistingCartItem(cartItemPostVm,existingCartItem))
                    // có chức năng tuowg t nhuw orElse nhưng nó thực thi theo cơ chế Lazi
                    .orElseGet(()-> createNewCartItem(cartItemPostVm,customerId));

        }catch (PessimisticLockingFailureException e){
            // throw exeption
            return null;
        }

    }
    private CartItem updateForCaseExistingCartItem(CartItemPostVm cartItemPostVm, CartItem existingCartItem){
        existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemPostVm.quantity());
        return cartItemRepository.save(existingCartItem);
    }
    private CartItem createNewCartItem(CartItemPostVm cartItemPostVm,String customerId){
        CartItem cartItemEntity = this.cartItemMapper.toCartItem(cartItemPostVm,customerId);
        return cartItemRepository.save(cartItemEntity);

    }




    // api put cartItem about quantity
    public CartItemGetVm updateCartItem(Long productId, CartItemPutVm cartItemPutVm){
        this.validateProduct(productId);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        CartItem cartItem = cartItemMapper.toCartItem(customerId,productId, cartItemPutVm.quantity());
        CartItem cartItemSaved = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemGetVm(cartItemSaved);

    }


    // api get cartItem
    public List<CartItemGetVm> getCartItems(){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<CartItem> cartItems = cartItemRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        return cartItemMapper.toCartItemGetVmList(cartItems);

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
