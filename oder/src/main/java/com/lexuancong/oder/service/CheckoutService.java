package com.lexuancong.oder.service;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.model.enum_status.CheckoutStatus;
import com.lexuancong.oder.repository.CheckoutRepository;
import com.lexuancong.oder.viewmodel.checkout.CheckoutPostVm;
import com.lexuancong.oder.viewmodel.checkout.CheckoutVm;
import com.lexuancong.oder.viewmodel.checkout.checkoutitem.CheckoutItemPostVm;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CheckoutRepository checkoutRepository;

    public CheckoutVm createCheckout(CheckoutPostVm checkoutPostVm){
        Checkout checkout = checkoutPostVm.toModel();
        checkout.setCheckoutStatus(CheckoutStatus.PENDING);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        checkout.setCustomerId(customerId);

        this.buildCheckoutItems(checkoutPostVm,checkout);
        // save checkout => checkout_Item =>save theo
        checkout = this.checkoutRepository.save(checkout);
        CheckoutVm checkoutVm = CheckoutVm.fromModel(checkout);
        return checkoutVm;
    }
    public void buildCheckoutItems(CheckoutPostVm checkoutPostVm , Checkout checkout){
        List<CheckoutItemPostVm> checkoutItemPostVms = checkoutPostVm.checkoutItemPostVms();
        List<CheckoutItem> checkoutItems = checkoutItemPostVms.stream()
                .map(checkoutItemPostVm -> checkoutItemPostVm.toModel(checkout) )
                .toList();
        checkout.setCheckoutItems(checkoutItems);
    }
}
