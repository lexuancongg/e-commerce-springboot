package com.lexuancong.oder.service;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.repository.OderRepository;
import com.lexuancong.oder.repository.OrderItemRepository;
import com.lexuancong.oder.viewmodel.OrderPostVm;
import com.lexuancong.oder.viewmodel.OrderVm;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OderService {
    private final OderRepository oderRepository;
    private final OrderItemRepository orderItemRepository;

    // tự thêm bean cần thieeys vào
    public OderService(OderRepository oderRepository, OrderItemRepository orderItemRepository) {
        this.oderRepository = oderRepository;
        this.orderItemRepository = orderItemRepository;
    }
    public OrderVm createOrder(OrderPostVm orderPostVm){
        Order order = orderPostVm.toModel();
        this.oderRepository.save(order); // shipping address đã lưu cùng oder
        Set<OrderItem> orderItemSet = orderPostVm.orderItemPostVms().stream()
                .map(orderItemPostVm -> orderItemPostVm.toModel(order))
                .collect(Collectors.toSet());
        // save orderItems
        this.orderItemRepository.saveAll(orderItemSet);

        OrderVm orderVm =
















    }

}
