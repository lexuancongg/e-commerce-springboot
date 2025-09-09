'use client';
import { OrderStatus } from "@/models/order/OrderStatus";
import { useEffect, useState } from "react";
import { OrderVm } from "@/models/order/OrderVm";
import orderService from "@/services/order/orderService";
import { ProductPreviewVm } from "@/models/product/ProductPreviewVm";
import productService from "@/services/product/productService";
import { DeliveryStatus } from "@/models/order/DeliveryStatus";
import dayjs from "dayjs";
import { DeliveryMethod } from "@/models/order/DeliveryMethod";
import OrderCard from "./orderCard";

type Props = {
    orderStatus: OrderStatus | null;
};

const demoOrders: OrderVm[] = [
    // Dữ liệu demo của mày, giữ nguyên
    {
        id: 1,
        orderStatus: OrderStatus.PENDING,
        totalPrice: 500000,
        deliveryStatus: DeliveryStatus.PENDING,
        orderItemVms: [
            {
                id: 101,
                productId: 1,
                productName: "Áo thun nam",
                quantity: 2,
                productPrice: 250000,
                productAvatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-02.jpg",
                totalPrice: 500000,
            },
        ],
        createdAt: dayjs("2024-03-27T17:00:00Z"),
        deliveryMethod: DeliveryMethod.VIETTEL_POST,
    },
    {
        id: 2,
        orderStatus: OrderStatus.CANCELLED,
        totalPrice: 800000,
        deliveryStatus: DeliveryStatus.DELIVERING,
        orderItemVms: [
            {
                id: 101,
                productId: 1,
                productName: "Áo thun nam",
                quantity: 2,
                productPrice: 250000,
                productAvatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-01.jpg",
                totalPrice: 500000,
            },
            {
                id: 102,
                productId: 2,
                productName: "Giày thể thao",
                quantity: 1,
                productPrice: 800000,
                productAvatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-02.jpg",
                totalPrice: 800000,
            },
        ],
        createdAt: dayjs("2024-03-26T15:30:00Z"),
        deliveryMethod: DeliveryMethod.VIETTEL_POST,
    },
];

export default function OrderStatusTab({ orderStatus }: Props) {
    const [orders, setOrders] = useState<OrderVm[]>(demoOrders);

    useEffect(() => {
        setOrders(demoOrders.filter(o => !orderStatus || o.orderStatus === orderStatus)); // Filter theo tab
    }, [orderStatus]);

    if (orders.length === 0) {
        return (
            <div className="text-center py-12">
                <div className="text-gray-400 mb-4">
                    <svg className="mx-auto h-12 w-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                    </svg>
                </div>
                <h3 className="text-lg font-medium text-gray-900">No Orders</h3>
                <p className="text-gray-500 mt-1">Chưa có đơn hàng nào ở trạng thái này.</p>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {orders.map((order) => (
                <OrderCard key={order.id} order={order} />
            ))}
        </div>
    );
}