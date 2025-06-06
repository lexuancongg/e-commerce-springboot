package com.lexuancong.oder.controller;

import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.service.OderService;
import com.lexuancong.oder.viewmodel.order.OrderDetailVm;
import com.lexuancong.oder.viewmodel.order.OrderPostVm;
import com.lexuancong.oder.viewmodel.order.OrderVm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class OrderController {
    private final OderService orderService;
    public OrderController(OderService orderService) {
        this.orderService = orderService;

    }

    // khách hàng mua sp
    @PostMapping("/customer/orders")
    public ResponseEntity<OrderVm> createOrder(@RequestBody @Valid OrderPostVm orderPostVm) {
        return  ResponseEntity.ok(orderService.createOrder(orderPostVm));

    }

    @GetMapping("/customer/orders/my-orders")
    public ResponseEntity<List<OrderDetailVm>> getMyOrders(
            @RequestParam(required = false)OrderStatus orderStatus
            ) {
        return ResponseEntity.ok(this.orderService.getMyOrders(orderStatus));

    }





}
