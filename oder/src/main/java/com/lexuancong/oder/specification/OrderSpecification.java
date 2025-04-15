package com.lexuancong.oder.specification;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.utils.Constants;
import org.springframework.data.jpa.domain.Specification;

// dùng trong trường hợp filter động , nhieeuf điều kiện
public class OrderSpecification {

    public static Specification<Order> findMyOrders(final String customerId,OrderStatus orderStatus){
        Specification<Order> specs = Specification
                .where(hasCreatedBy(customerId))
                .and(hasOrderStatus(orderStatus));
        return specs;
    }

    public static Specification<Order> hasCreatedBy(final String customerId){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(Constants.Column.CREATE_BY_COLUMN), customerId);
        };
    }

    public static Specification<Order> hasOrderStatus(final OrderStatus orderStatus){
        return (root, query, criteriaBuilder) -> {
            return orderStatus == null ? criteriaBuilder.conjunction()
                    : criteriaBuilder.equal(root.get(Constants.Column.ORDER_STATUS_COLUMN), orderStatus);
        };

    }

}
