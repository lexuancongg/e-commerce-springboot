package com.lexuancong.oder.service;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.repository.OrderRepository;
import com.lexuancong.oder.repository.OrderItemRepository;
import com.lexuancong.oder.service.internal.CartService;
import com.lexuancong.oder.service.internal.ProductService;
import com.lexuancong.oder.specification.OrderSpecification;
import com.lexuancong.oder.constants.Constants;
import com.lexuancong.oder.viewmodel.order.CheckUserHasBoughtProductCompletedVm;
import com.lexuancong.oder.viewmodel.order.OrderDetailVm;
import com.lexuancong.oder.viewmodel.order.OrderPostVm;
import com.lexuancong.oder.viewmodel.order.OrderVm;
import com.lexuancong.oder.viewmodel.product.ProductVariantPreviewVm;
import com.lexuancong.share.utils.AuthenticationUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    public OderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    public OrderVm createOrder(OrderPostVm orderPostVm){
        Order order = orderPostVm.toModel();
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        order.setCustomerId(userId);

        this.orderRepository.save(order);
        Set<OrderItem> orderItemSet = orderPostVm.orderItemPostVms().stream()
                .map(orderItemPostVm -> orderItemPostVm.toModel(order))
                .collect(Collectors.toSet());
        // save orderItems
        this.orderItemRepository.saveAll(orderItemSet);



        OrderVm orderVm = OrderVm.fromModel(order,orderItemSet);

        this.cartService.deleteCartItems(orderItemSet);
        this.productService.updateQuantityProductAfterOrder(orderItemSet);
        return orderVm;


    }
    private void updateOderStatus(Long orderId, OrderStatus orderStatus){
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("Order not found"));
        order.setOderStatus(orderStatus);
        this.orderRepository.save(order);
    }

    public List<OrderDetailVm> getMyOrders(OrderStatus orderStatus){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Specification<Order> specification = OrderSpecification.findMyOrders(userId,orderStatus);
        Sort sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_AT_COLUMN);
        List<Order> orders = this.orderRepository.findAll(specification,sort);
        return orders.stream()
                .map(order -> {
                    Long orderId = order.getId();
                    List<OrderItem> orderItems = this.orderItemRepository.findAllByOderId(orderId);
                    return OrderDetailVm.fromModel(order,orderItems);
                })
                .toList();

    }


    // product này là product cha
    public CheckUserHasBoughtProductCompletedVm checkUserHasBoughtProductCompleted(Long productId){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<ProductVariantPreviewVm> productVariantPreviewVms =  this.productService.getProductVariantByProductParentId(productId);
        List<Long> productVariantIds = productVariantPreviewVms.stream()
                .map(ProductVariantPreviewVm::id)
                .toList();


        Specification<Order> checkUserHasBoughtSpecification =
                OrderSpecification.checkUserHasBoughtProductCompleted(productVariantIds,customerId);
        boolean hasPurchased = this.orderRepository.findOne(checkUserHasBoughtSpecification)
                .isPresent();
        return new CheckUserHasBoughtProductCompletedVm(hasPurchased);








    }

}
