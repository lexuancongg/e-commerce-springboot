package com.lexuancong.oder.service;

import com.lexuancong.oder.constants.Constants;
import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.model.enum_status.CheckoutStatus;
import com.lexuancong.oder.repository.CheckoutRepository;
import com.lexuancong.oder.service.internal.ProductService;
import com.lexuancong.oder.viewmodel.checkout.CheckoutPostVm;
import com.lexuancong.oder.viewmodel.checkout.CheckoutVm;
import com.lexuancong.oder.viewmodel.checkout.checkoutitem.CheckoutItemPostVm;
import com.lexuancong.oder.viewmodel.checkout.checkoutitem.CheckoutItemVm;
import com.lexuancong.oder.viewmodel.product.ProductCheckoutPreviewVm;
import com.lexuancong.share.exception.AccessDeniedException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.math.BigDecimal;
import java.util.HashMap;
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

    public CheckoutVm createCheckout(CheckoutPostVm checkoutPostVm){
        Checkout checkout = checkoutPostVm.toModel();
        checkout.setCheckoutStatus(CheckoutStatus.PENDING);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        checkout.setCustomerId(customerId);
        List<CheckoutItem> checkoutItems = this.buildCheckoutItems(checkoutPostVm,checkout);
        checkout.setCheckoutItems(checkoutItems);
        BigDecimal total = checkoutItems.parallelStream()
                .reduce(BigDecimal.ZERO,
                        (acc, item) -> acc.add(item.getPrice()),
                        BigDecimal::add); // gộp kết quả từ nhiều thread
        checkout.setTotalAmount(total);

        checkout = this.checkoutRepository.save(checkout);
        CheckoutVm checkoutVm = CheckoutVm.fromModel(checkout);
        return checkoutVm;
    }


    public List<CheckoutItem> buildCheckoutItems(CheckoutPostVm checkoutPostVm , Checkout checkout){
        List<CheckoutItemPostVm> checkoutItemPostVms = checkoutPostVm.checkoutItemPostVms();
        Set<Long> productIdsCheckoutItems = checkoutItemPostVms.stream()
                .map(CheckoutItemPostVm::productId)
                .collect(Collectors.toSet());

        List<ProductCheckoutPreviewVm> productCheckoutPreviewVms =
                this.productService.getProductInfoPreviewByIds(productIdsCheckoutItems);
        if(productCheckoutPreviewVms.isEmpty()){
            throw  new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
        }
        Map<Long,ProductCheckoutPreviewVm> productCheckoutPreviewVmMap = productCheckoutPreviewVms.stream()
                .collect(Collectors.toMap(ProductCheckoutPreviewVm::id,Function.identity()));


        List<CheckoutItem> checkoutItems = checkoutItemPostVms.stream()
                .map(checkoutItemPostVm -> {
                    ProductCheckoutPreviewVm productInfo = productCheckoutPreviewVmMap.get(checkoutItemPostVm.productId());
                    if(productInfo == null){
                        throw  new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND);
                    }
                    return checkoutItemPostVm.toModel(checkout,productInfo);
                } )
                .toList();
        return checkoutItems;
    }



    public CheckoutVm getCheckoutById(Long id){
        Checkout checkout = this.checkoutRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.CHECKOUT_NOT_FOUND));
        this.validateOwnCurrentUser(checkout);
        return CheckoutVm.fromModel(checkout);

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
