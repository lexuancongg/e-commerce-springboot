package com.lexuancong.oder.service;

import com.lexuancong.oder.constants.Constants;
import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.repository.CheckoutRepository;
import com.lexuancong.oder.service.internal.ProductService;
import com.lexuancong.oder.dto.checkout.CheckoutCreateRequest;
import com.lexuancong.oder.dto.checkout.CheckoutGetResponse;
import com.lexuancong.oder.dto.checkoutitem.CheckoutItemCreateRequest;
import com.lexuancong.oder.dto.product.ProductCheckoutPreviewGetResponse;
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
    private final ProductService productService;

    public CheckoutGetResponse createCheckout(CheckoutCreateRequest checkoutCreateRequest){
        Checkout checkout = checkoutCreateRequest.toModel();
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        checkout.setCustomerId(customerId);
        List<CheckoutItem> checkoutItems = this.buildCheckoutItems(checkoutCreateRequest,checkout);
        checkout.setCheckoutItems(checkoutItems);
        BigDecimal total = checkoutItems.parallelStream()
                .reduce(BigDecimal.ZERO,
                        (acc, item) -> acc.add(item.getPrice()),
                        BigDecimal::add); // gộp kết quả từ nhiều thread
        checkout.setTotalPrice(total);

        checkout = this.checkoutRepository.save(checkout);
        CheckoutGetResponse checkoutGetResponse = CheckoutGetResponse.fromModel(checkout);
        return checkoutGetResponse;
    }


    public List<CheckoutItem> buildCheckoutItems(CheckoutCreateRequest checkoutCreateRequest, Checkout checkout){
        List<CheckoutItemCreateRequest> checkoutItemCreateRequests = checkoutCreateRequest.checkoutItemCreateRequests();
        Set<Long> productIdsCheckoutItems = checkoutItemCreateRequests.stream()
                .map(CheckoutItemCreateRequest::productId)
                .collect(Collectors.toSet());

        List<ProductCheckoutPreviewGetResponse> productCheckoutPreviewGetResponses =
                this.productService.getProductInfoPreviewByIds(productIdsCheckoutItems);
        if(productCheckoutPreviewGetResponses.isEmpty()){
            throw  new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
        }
        Map<Long, ProductCheckoutPreviewGetResponse> productCheckoutPreviewVmMap = productCheckoutPreviewGetResponses.stream()
                .collect(Collectors.toMap(ProductCheckoutPreviewGetResponse::id,Function.identity()));


        List<CheckoutItem> checkoutItems = checkoutItemCreateRequests.stream()
                .map(checkoutItemCreateRequest -> {
                    ProductCheckoutPreviewGetResponse productInfo = productCheckoutPreviewVmMap.get(checkoutItemCreateRequest.productId());
                    if(productInfo == null){
                        throw  new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
                    }
                    return checkoutItemCreateRequest.toCheckoutItem(checkout,productInfo);
                } )
                .toList();
        return checkoutItems;
    }



    public CheckoutGetResponse getCheckoutById(Long id){
        Checkout checkout = this.checkoutRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.CHECKOUT_NOT_FOUND));
        this.validateOwnCurrentUser(checkout);
        return CheckoutGetResponse.fromModel(checkout);

    }

    private void validateOwnCurrentUser(Checkout checkout){
        if(this.checkOwnCurrentUser(checkout)){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED);
        }
    }
    private boolean checkOwnCurrentUser(Checkout checkout){
        return !checkout.getId().equals(AuthenticationUtils.extractCustomerIdFromJwt());

    }
}
