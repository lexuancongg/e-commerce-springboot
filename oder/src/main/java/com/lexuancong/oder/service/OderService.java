package com.lexuancong.oder.service;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.repository.OrderRepository;
import com.lexuancong.oder.repository.OrderItemRepository;
import com.lexuancong.oder.service.internal.CartClient;
import com.lexuancong.oder.service.internal.InventoryService;
import com.lexuancong.oder.service.internal.ProductClient;
import com.lexuancong.oder.specification.OrderSpecification;
import com.lexuancong.oder.constants.Constants;
import com.lexuancong.oder.dto.inventory.InventorySubtract;
import com.lexuancong.oder.dto.order.*;
import com.lexuancong.oder.dto.product.ProductVariantInfoResponse;
import com.lexuancong.share.dto.paging.PagingResponse;
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
    private final CartClient cartClient;
    private final ProductClient productClient;
    private final InventoryService inventoryService;
    public OderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartClient cartClient, ProductClient productClient, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartClient = cartClient;
        this.productClient = productClient;
        this.inventoryService = inventoryService;
    }

    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest){
        List<InventorySubtract> inventorySubtracts = new ArrayList<>();

        Order order = orderCreateRequest.toOrder();
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        order.setCustomerId(userId);

        this.orderRepository.save(order);

        List<OrderItem> orderItems = orderCreateRequest.items().stream()
                .map(orderItemCreateRequest -> {
                    inventorySubtracts.add(orderItemCreateRequest.toInventorySubtract());
                    return  orderItemCreateRequest.toOrderItem(order);
                })
                .toList();

        this.orderItemRepository.saveAll(orderItems);

        // dùng kafka để nhanh hơn
        this.cartClient.deleteCartItems(orderItems);

        this.productClient.updateQuantityProductAfterOrder(orderItems);
        this.inventoryService.subtractQuantityProduct(inventorySubtracts);
        return  OrderResponse.from(order,orderItems);

    }




    public List<OrderResponse> getMyOrders(OrderStatus orderStatus){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        Specification<Order> specification = OrderSpecification.findMyOrders(customerId,orderStatus);
        Sort sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_AT_COLUMN);
        List<Order> orders = this.orderRepository.findAll(specification,sort);
        return orders.stream()
                .map(order -> {
                    Long orderId = order.getId();
                    List<OrderItem> orderItems = this.orderItemRepository.findAllByOderId(orderId);
                    return OrderResponse.from(order,orderItems);
                })
                .toList();

    }


    // product này là product cha
    public UserPurchasedProductResponse checkUserHasBoughtProductCompleted(Long productId){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<ProductVariantInfoResponse> productVariantInfos = this.productClient.getProductVariantByProductParentId(productId);
        List<Long> productVariantIds = productVariantInfos.stream()
                .map(ProductVariantInfoResponse::id)
                .toList();


        Specification<Order> checkUserHasBoughtSpecification =
                OrderSpecification.checkUserHasBoughtProductCompleted(productVariantIds,customerId);
        boolean hasPurchased = this.orderRepository.findOne(checkUserHasBoughtSpecification)
                .isPresent();
        return new UserPurchasedProductResponse(hasPurchased);








    }

    public PagingResponse<OrderPreviewResponse> getOrders(int pageIndex, int pageSize){
        Sort sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_AT_COLUMN);
        Pageable   pageable = PageRequest.of(pageIndex,pageSize,sort);
        Page<Order> pageOrders = this.orderRepository.findAll(pageable);
        List<Order> orders = pageOrders.getContent();
        List<OrderPreviewResponse> orderPreviews = orders.stream()
                .map(OrderPreviewResponse::fromOrder)
                .toList();

        return  PagingResponse.<OrderPreviewResponse>builder()
                .payload(orderPreviews)
                .last(pageOrders.isLast())
                .totalPages(pageOrders.getTotalPages())
                .totalElements(pageOrders.getTotalElements())
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();

    }


    public OrderResponse getOrderById(Long orderId){
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.ORDER_NOT_FOUND,orderId));
        List<OrderItem> orderItems = this.orderItemRepository.findAllByOderId(orderId);
        return OrderResponse.from(order,orderItems);

    }

    public void  updateOrderStatus(Long id, OrderStatus orderStatus){
        Order order = this.orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.ORDER_NOT_FOUND,id));
        order.setOderStatus(orderStatus);
        this.orderRepository.save(order);
    }

}
