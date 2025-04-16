package com.lexuancong.oder.service;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.model.enum_status.CheckoutStatus;
import com.lexuancong.oder.utils.AuthenticationUtils;
import com.lexuancong.oder.viewmodel.checkout.CheckoutPostVm;
import com.lexuancong.oder.viewmodel.checkout.CheckoutVm;
import com.lexuancong.oder.viewmodel.checkout.checkoutitem.CheckoutItemPostVm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    private
    public CheckoutVm createCheckout(CheckoutPostVm checkoutPostVm){
        Checkout checkout = checkoutPostVm.toModel();
        checkout.setCheckoutStatus(CheckoutStatus.PENDING);
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        checkout.setCustomerId(customerId);
        this.buildCheckoutItems(checkoutPostVm,checkout);
        checkout = this.


    }
    public void buildCheckoutItems(CheckoutPostVm checkoutPostVm , Checkout checkout){
        List<CheckoutItemPostVm> checkoutItemPostVms = checkoutPostVm.checkoutItemPostVms();
        List<CheckoutItem> checkoutItems = checkoutItemPostVms.stream()
                .map(checkoutItemPostVm -> checkoutItemPostVm.toModel(checkout) )
                .toList();
        checkout.setCheckoutItems(checkoutItems);
    }
}
