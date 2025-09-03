package com.lexuancong.oder.constants;

import jakarta.persistence.PreUpdate;

public final  class Constants {
    public final class PagingConstants{
        public static final String DEFAULT_PAGE_NUMBER = "0";
        public static final String DEFAULT_PAGE_SIZE = "10";

    }
    public final class  Column{
        public static final String CREATE_AT_COLUMN = "createdAt";
        public static final String CREATE_BY_COLUMN = "createdBy";
        public static final String ORDER_STATUS_COLUMN ="orderStatus";
        public static final String ORDER_ID_COLUMN ="orderId";
        public static final String ID_COLUMN = "id";
        public static final String PRODUCT_ID_COLUMN ="productId";
    }
    public final class ErrorKey{
        public static final String PRODUCT_NOT_FOUND ="PRODUCT_NOT_FOUND";
        public static final String CHECKOUT_NOT_FOUND ="CHECKOUT_NOT_FOUND";
        public static final String ACCESS_DENIED ="ACCESS_DENIED";
    }
}
