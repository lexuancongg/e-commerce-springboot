package com.lexuancong.oder.service;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.repository.OderRepository;
import com.lexuancong.oder.repository.OrderItemRepository;
import com.lexuancong.oder.service.internal.CartService;
import com.lexuancong.oder.utils.Constants;
import com.lexuancong.oder.viewmodel.order.OrderGetVm;
import com.lexuancong.oder.viewmodel.order.OrderPostVm;
import com.lexuancong.oder.viewmodel.order.OrderVm;
import lombok.extern.java.Log;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OderService {
    private final OderRepository oderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    // tự thêm bean cần thieeys vào
    public OderService(OderRepository oderRepository, OrderItemRepository orderItemRepository, CartService cartService) {
        this.oderRepository = oderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }
    public OrderVm createOrder(OrderPostVm orderPostVm){
        Order order = orderPostVm.toModel();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setCustomerId(userId);

        this.oderRepository.save(order); // shipping address đã lưu cùng oder
        Set<OrderItem> orderItemSet = orderPostVm.orderItemPostVms().stream()
                .map(orderItemPostVm -> orderItemPostVm.toModel(order))
                .collect(Collectors.toSet());
        // save orderItems
        this.orderItemRepository.saveAll(orderItemSet);



        OrderVm orderVm = OrderVm.fromModel(order,orderItemSet);
        // xóa sản phẩm trong giỏ hàng đi
        this.cartService.deleteCartItems(orderVm);
        // tru số lượng hàng tồn cho sp


        // cap nhat trang thai don hang
        this.updateOderStatus(order.getId(),OrderStatus.CONFIRMED);
        return orderVm;


    }
    private void updateOderStatus(Long orderId, OrderStatus orderStatus){
        Order order = this.oderRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("Order not found"));
        order.setOderStatus(orderStatus);
        this.oderRepository.save(order);
    }

    public List<OrderGetVm> getMyOrders(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_AT_COLUMN);
        List<Order> orders = this.oderRepository.findAllByCustomerId(userId,sort);
        return orders.stream()
                .map(order -> {
                    Long orderId = order.getId();
                    List<OrderItem> orderItems = this.orderItemRepository.findAllByOderId(orderId);
                    return OrderGetVm.fromModel(order,orderItems);
                })
                .toList();

    }

}
