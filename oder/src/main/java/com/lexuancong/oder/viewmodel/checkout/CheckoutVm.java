package com.lexuancong.oder.viewmodel.checkout;

import com.lexuancong.oder.viewmodel.checkout.checkoutitem.CheckoutItemVm;

import java.util.List;

public record CheckoutVm (
        String email,
        List<CheckoutItemVm> checkoutItemVms

){


}
