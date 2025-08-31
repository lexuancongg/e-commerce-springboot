package com.lexuancong.oder.specification;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.constants.Constants;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

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


    public static Specification<Order> checkUserHasBoughtProductCompleted(List<Long> productIds, String customerId){
        return Specification.where(hasCreatedBy(customerId))
                .and(hasOrderStatus(OrderStatus.COMPLETED))
                .and(orderItemsConstantProduct(productIds));
    }

    public static Specification<Order> orderItemsConstantProduct(List<Long> productIds){
        return (root, query, criteriaBuilder) -> {
            Subquery<OrderItem> subquery = query.subquery(OrderItem.class);
            Root<OrderItem> orderItemRoot = subquery.from(OrderItem.class);
            subquery.select(orderItemRoot)
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(
                                            orderItemRoot.get(Constants.Column.ORDER_ID_COLUMN),
                                            root.get(Constants.Column.ID_COLUMN)

                                    ),
                                    orderItemRoot.get(Constants.Column.PRODUCT_ID_COLUMN)
                                            .in(productIds)

                            )
                    );
            return criteriaBuilder.exists(subquery);
        };
    }

}



// docs
// root là table ,
// Predicate là đại diện cho một caau điều kiện hoặc chuổi câu điều kiện kết nối bằng and haowjc or ,
// Specification : đại diện cho toàn bộ câu query

// Subquery tạo ra truy vấn con ví dụ : select * from id in (select *..)
