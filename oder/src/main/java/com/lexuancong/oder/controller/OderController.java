package com.lexuancong.oder.controller;

import com.lexuancong.oder.service.OderService;
import com.lexuancong.oder.viewmodel.OrderPostVm;
import com.lexuancong.oder.viewmodel.OrderVm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class OderController {
    private final OderService orderService;
    public OderController(OderService orderService) {
        this.orderService = orderService;

    }

    // khách hàng mua sp
    @PostMapping("/store/orders")
    public ResponseEntity<OrderVm> createOrder(@RequestBody @Valid OrderPostVm orderPostVm) {
        return this.orderService.createOrder(orderPostVm);

    }


}
