package com.lexuancong.oder.service;

import com.lexuancong.oder.constants.Constants;
import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.repository.CheckoutRepository;
import com.lexuancong.oder.service.internal.ProductClient;
import com.lexuancong.oder.dto.checkout.CheckoutCreateRequest;
import com.lexuancong.oder.dto.checkout.CheckoutResponse;
import com.lexuancong.oder.dto.checkoutitem.CheckoutItemCreateRequest;
import com.lexuancong.oder.dto.product.ProductInfoResponse;
import com.lexuancong.share.exception.AccessDeniedException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CheckoutRepository checkoutRepository;
    private final ProductClient productClient;

    public CheckoutResponse createCheckout(CheckoutCreateRequest checkoutCreateRequest){
        Checkout checkout = checkoutCreateRequest.toCheckout();
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();

        checkout.setCustomerId(customerId);
        List<CheckoutItem> checkoutItems = this.buildCheckoutItems(checkoutCreateRequest,checkout);

        checkout.setCheckoutItems(checkoutItems);

        BigDecimal total = checkoutItems.parallelStream()
                .reduce(BigDecimal.ZERO,
                        (acc, item) -> acc.add(item.getPrice()),
                        BigDecimal::add);
        checkout.setTotalPrice(total);

        checkout = this.checkoutRepository.save(checkout);
        CheckoutResponse checkoutResponse = CheckoutResponse.fromCheckout(checkout);
        return checkoutResponse;
    }


    public List<CheckoutItem> buildCheckoutItems(CheckoutCreateRequest checkoutCreateRequest, Checkout checkout){
        List<CheckoutItemCreateRequest> checkoutItemCreateRequests = checkoutCreateRequest.checkoutItemCreateRequests();
        Set<Long> productIdsCheckoutItems = checkoutItemCreateRequests.stream()
                .map(CheckoutItemCreateRequest::productId)
                .collect(Collectors.toSet());

        List<ProductInfoResponse> productInfos =
                this.productClient.getProductInfoByIds(productIdsCheckoutItems);
        if(productInfos.isEmpty()){
            throw  new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
        }

        Map<Long, ProductInfoResponse> mapProductInfo = productInfos.stream()
                .collect(Collectors.toMap(ProductInfoResponse::id,Function.identity()));


        List<CheckoutItem> checkoutItems = checkoutItemCreateRequests.stream()
                .map(checkoutItemCreateRequest -> {
                    ProductInfoResponse productInfo = mapProductInfo.get(checkoutItemCreateRequest.productId());
                    if(productInfo == null){
                        throw  new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
                    }
                    return checkoutItemCreateRequest.toCheckoutItem(checkout,productInfo);
                } )
                .toList();
        return checkoutItems;
    }



    public CheckoutResponse getCheckoutById(Long id){
        Checkout checkout = this.checkoutRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.CHECKOUT_NOT_FOUND));
        this.validateOwnCurrentUser(checkout);
        return CheckoutResponse.fromCheckout(checkout);

    }

    private void validateOwnCurrentUser(Checkout checkout){
        if(this.checkOwnCurrentUser(checkout)){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED);
        }
    }
    private boolean checkOwnCurrentUser(Checkout checkout){
        String customerIdCurrent = AuthenticationUtils.extractCustomerIdFromJwt();
        return !checkout.getCustomerId().equals(customerIdCurrent);

    }
}
