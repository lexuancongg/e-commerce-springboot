package com.lexuancong.oder.service;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.repository.OrderRepository;
import com.lexuancong.oder.repository.OrderItemRepository;
import com.lexuancong.oder.service.internal.CartService;
import com.lexuancong.oder.service.internal.InventoryService;
import com.lexuancong.oder.service.internal.ProductService;
import com.lexuancong.oder.specification.OrderSpecification;
import com.lexuancong.oder.constants.Constants;
import com.lexuancong.oder.dto.inventory.InventorySubtract;
import com.lexuancong.oder.dto.order.*;
import com.lexuancong.oder.dto.product.ProductVariantPreviewGetResponse;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.utils.AuthenticationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    public OderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ProductService productService, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    public OrderVm createOrder(OrderCreateRequest orderCreateRequest){
        List<InventorySubtract> inventorySubtracts = new ArrayList<>();
        Order order = orderCreateRequest.toModel();
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        order.setCustomerId(userId);

        this.orderRepository.save(order);
        List<OrderItem> orderItemSet = orderCreateRequest.orderItemCreateRequests().stream()
                .map(orderItemPostVm -> {
                    inventorySubtracts.add(orderItemPostVm.toInventorySubtract());
                    return  orderItemPostVm.toOrder(order);
                })
                .toList();
        // save orderItems
        this.orderItemRepository.saveAll(orderItemSet);


        OrderVm orderVm = OrderVm.fromModel(order,orderItemSet);

        this.cartService.deleteCartItems(orderItemSet);
        this.productService.updateQuantityProductAfterOrder(orderItemSet);
        this.inventoryService.subtractQuantityProduct(inventorySubtracts);
        return orderVm;


    }


    private void updateOderStatus(Long orderId, OrderStatus orderStatus){
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("Order not found"));
        order.setOderStatus(orderStatus);
        this.orderRepository.save(order);
    }

    public List<OrderVm> getMyOrders(OrderStatus orderStatus){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        Specification<Order> specification = OrderSpecification.findMyOrders(customerId,orderStatus);
        Sort sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_AT_COLUMN);
        List<Order> orders = this.orderRepository.findAll(specification,sort);
        return orders.stream()
                .map(order -> {
                    Long orderId = order.getId();
                    List<OrderItem> orderItems = this.orderItemRepository.findAllByOderId(orderId);
                    return OrderVm.fromModel(order,orderItems);
                })
                .toList();

    }


    // product này là product cha
    public CheckUserHasBoughtProductCompleted checkUserHasBoughtProductCompleted(Long productId){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<ProductVariantPreviewGetResponse> productVariantPreviewGetResponses =  this.productService.getProductVariantByProductParentId(productId);
        List<Long> productVariantIds = productVariantPreviewGetResponses.stream()
                .map(ProductVariantPreviewGetResponse::id)
                .toList();


        Specification<Order> checkUserHasBoughtSpecification =
                OrderSpecification.checkUserHasBoughtProductCompleted(productVariantIds,customerId);
        boolean hasPurchased = this.orderRepository.findOne(checkUserHasBoughtSpecification)
                .isPresent();
        return new CheckUserHasBoughtProductCompleted(hasPurchased);








    }

    public OrderPagingGetResponse getOrders(int pageIndex, int pageSize){
        Sort sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_AT_COLUMN);
        Pageable   pageable = PageRequest.of(pageIndex,pageSize,sort);
        Page<Order> pageOrders = this.orderRepository.findAll(pageable);
        List<Order> orders = pageOrders.getContent();
        List<OrderPreviewGetResponse> orderPreviewGetResponses = orders.stream()
                .map(OrderPreviewGetResponse::fromOrder)
                .toList();

        return new OrderPagingGetResponse(
                orderPreviewGetResponses,
                pageIndex,
                pageSize,
                (int) pageOrders.getTotalElements(),
                pageOrders.getTotalPages(),
                pageOrders.isLast()
        );

    }


    public OrderVm getOrderById(Long orderId){
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.ORDER_NOT_FOUND,orderId));
        List<OrderItem> orderItems = this.orderItemRepository.findAllByOderId(orderId);
        return OrderVm.fromModel(order,orderItems);

    }

    public void  updateOrderStatus(Long id, OrderStatus orderStatus){
        Order order = this.orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.ORDER_NOT_FOUND,id));
        order.setOderStatus(orderStatus);
        this.orderRepository.save(order);
    }

}
