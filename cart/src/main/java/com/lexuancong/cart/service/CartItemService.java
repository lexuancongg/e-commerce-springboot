package com.lexuancong.cart.service;

import com.lexuancong.cart.mapper.CartItemMapper;
import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.repository.CartItemRepository;
import com.lexuancong.cart.utils.AuthenticationUtils;
import com.lexuancong.cart.viewmodel.CartItemGetVm;
import com.lexuancong.cart.viewmodel.CartItemPostVm;
import com.lexuancong.cart.viewmodel.CartItemPutVm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;
    public CartItemGetVm addCartItem(CartItemPostVm cartItemPostVm){
        this.validateproduct(cartItemPostVm.productId());
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        CartItem cartItem = performAddCartItem(cartItemPostVm,customerId);
        return cartItemMapper.toCartItemGetVm(cartItem);

    }
    private void validateproduct(Long productId){
        if(!productService.checkExistById(productId)){
            // ban ra ngoai le

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
        this.validateproduct(productId);
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
        this.validateproduct(productId);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        cartItemRepository.deleteByCustomerIdAndProductId(customerId,productId);
    }
}
