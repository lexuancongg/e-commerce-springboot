'use client'
import { OrderStatus } from "@/models/order/OrderStatus";
import { useEffect, useState } from "react";
import { OrderDetailVm } from "@/models/order/OrderDetailVm";
import orderService from "@/services/order/orderService";
import { ProductPreviewVm } from "@/models/product/ProductPreviewVm";
import productService from "@/services/product/productService";
import { DeliveryStatus } from "@/models/order/DeliveryStatus";
import dayjs from "dayjs";
import { DeliveryMethod } from "@/models/order/DeliveryMethod";
import OrderCard from "./orderCard";
type Props = {
    orderStatus: OrderStatus | null
}


const orderss: OrderDetailVm[] = [
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
                productAvatarUrl: "https://example.com/image1.jpg",
                totalPrice: 500
            },
        ],
        createdAt: dayjs("2024-03-27T10:00:00Z"),
        deliveryMethod: DeliveryMethod.VIETTEL_POST
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
                productAvatarUrl: "https://example.com/image1.jpg",
                totalPrice: 500
            },
            {
                id: 102,
                productId: 2,
                productName: "Giày thể thao",
                quantity: 1,
                productPrice: 800000,
                productAvatarUrl: "https://example.com/image2.jpg",
                totalPrice: 500
            },
        ],
        createdAt: dayjs("2024-03-26T15:30:00Z"),
        deliveryMethod: DeliveryMethod.VIETTEL_POST
    },
  ];
  
export default function OrderStatusTab({ orderStatus }: Props) {
    const [orders, setOrders] = useState<OrderDetailVm[]>(orderss)


    useEffect(() => {
        orderService.getMyOrder(orderStatus)
            .then(async (orderVmResults) => {
                const productIds: number[] = orderVmResults.flatMap((orderVmResult) => {
                    return orderVmResult.orderItemVms.map(orderItem => {
                        return orderItem.productId;
                    })
                })

                if (orderVmResults.length) {
                    const productPreviewVms: ProductPreviewVm[] = await productService.getProductsByIds(productIds);
                    orderVmResults.forEach((orderVm) => {
                        orderVm.orderItemVms.forEach((orderItemVm) => {
                            const productPreview: ProductPreviewVm = productPreviewVms.find(
                                (product) => product.id == orderItemVm.productId)!;
                            orderItemVm.productAvatarUrl = productPreview.avatarUrl;

                        })
                    })
                }
                setOrders(orderVmResults);

            })

    }, [orderStatus])

    return (
        <div>
            {
                orders.length ? (
                    orders.map((order) => {
                        return (
                            <OrderCard order={order} key={order.id}></OrderCard>
                        )
                    })
                ) :
                    (
                        <div>
                            <h3 style={{ textAlign: 'center' }}>No Orders</h3>
                        </div>
                    )
            }
        </div>



    );
}