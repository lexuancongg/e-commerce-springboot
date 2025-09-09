'use client';
import { OrderVm } from "@/models/order/OrderVm";

import dayjs from "dayjs";
import Link from "next/link";
import {DeliveryStatus} from "@/models/order/DeliveryStatus";

interface Props {
    order: OrderVm;
}

const getStatusColor = (status: string) => {
    const colors: { [key: string]: string } = {
        PENDING: 'bg-yellow-100 text-yellow-800',
        CONFIRMED: 'bg-blue-100 text-blue-800',
        PROCESSING: 'bg-indigo-100 text-indigo-800',
        PACKAGED: 'bg-purple-100 text-purple-800',
        SHIPPED: 'bg-green-100 text-green-800',
        'OUT_FOR_DELIVERY': 'bg-orange-100 text-orange-800',
        DELIVERED: 'bg-emerald-100 text-emerald-800',
        CANCELLED: 'bg-red-100 text-red-800',
        RETURNED: 'bg-gray-100 text-gray-800',
        REFUNDED: 'bg-pink-100 text-pink-800',
        FAILED: 'bg-rose-100 text-rose-800',
        'RETURN_REQUESTED': 'bg-amber-100 text-amber-800',
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
};

const getDeliveryColor = (status: DeliveryStatus) => {
    switch (status) {
        case DeliveryStatus.PENDING: return 'bg-yellow-100 text-yellow-800';
        case DeliveryStatus.DELIVERING: return 'bg-blue-100 text-blue-800';
        case DeliveryStatus.DELIVERED: return 'bg-green-100 text-green-800';
        default: return 'bg-gray-100 text-gray-800';
    }
};

const DeliveryIcon = ({ status }: { status: DeliveryStatus }) => {
    if (status === DeliveryStatus.DELIVERING) {
        return (
            <div className="flex items-center">
                <div className="w-2 h-2 bg-red-500 rounded-full mr-1 animate-pulse"></div> {/* Error dot như screenshot */}
                <span>1 error</span>
            </div>
        );
    }
    return <span className="text-sm text-gray-500">✓</span>;
};

export default function OrderCard({ order }: Props) {
    const formatPrice = (price: number) => `${price.toLocaleString('vi-VN')} đ`;
    const orderDate = dayjs(order.createdAt).format('DD/MM/YYYY HH:mm');

    const deliveryProgress = () => {
        const steps = [
            { status: DeliveryStatus.PENDING, label: 'Pending' },
            { status: DeliveryStatus.DELIVERING, label: 'Delivering' },
            { status: DeliveryStatus.DELIVERED, label: 'Delivered' },
        ];
        const currentIndex = steps.findIndex(s => s.status === order.deliveryStatus);
        return (
            <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                    className="bg-blue-600 h-2 rounded-full transition-all duration-300"
                    style={{ width: `${((currentIndex + 1) / steps.length) * 100}%` }}
                ></div>
            </div>
        );
    };

    return (
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200 overflow-hidden">
            <div className="p-6 border-b border-gray-100">
                <div className="flex justify-between items-start mb-4">
                    <div>
                        <p className="text-sm text-gray-500 mt-1">{orderDate}</p>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.orderStatus)}`}>
                        {order.orderStatus}
                    </span>
                </div>

                {/* Delivery Progress */}
                {order.deliveryStatus && (
                    <div className="space-y-2">
                        <div className="flex justify-between text-sm">
                            <span className="text-gray-600">Delivery Status</span>
                            <span className={`px-2 py-1 rounded-full text-xs font-medium ${getDeliveryColor(order.deliveryStatus)}`}>
                                {order.deliveryStatus}
                            </span>
                        </div>
                        {deliveryProgress()}
                        {order.deliveryMethod && (
                            <p className="text-sm text-gray-500 flex items-center">
                                <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                                </svg>
                                Delivery Method: {order.deliveryMethod.replace('_', ' ')}
                            </p>
                        )}
                    </div>
                )}
            </div>

            {/* Items List */}
            <div className="p-6">
                <div className="space-y-4">
                    {order.orderItemVms.map((item: any) => (
                        <div key={item.id} className="flex items-center space-x-4">
                            <img
                                src={item.productAvatarUrl || "https://via.placeholder.com/80x80?text=Product"}
                                alt={item.productName}
                                className="w-16 h-16 rounded-lg object-cover flex-shrink-0"
                            />
                            <div className="flex-1 min-w-0">
                                <Link href={`/product/${item.productId}`} className="block text-sm font-medium text-gray-900 hover:text-blue-600 truncate">
                                    {item.productName}
                                </Link>
                                <p className="text-sm text-gray-500">Qty: {item.quantity}</p>
                            </div>
                            <div className="text-right">
                                <p className="text-sm font-semibold text-gray-900">{formatPrice(item.productPrice)}</p>
                                <p className="text-sm text-gray-500">Total: {formatPrice(item.totalPrice)}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>


            <div className="px-6 py-4 bg-gray-50 border-t border-gray-100 flex justify-between items-center">
                <div className="flex items-center space-x-2">
                    {order.deliveryStatus === DeliveryStatus.DELIVERING && <DeliveryIcon status={order.deliveryStatus} />}
                </div>
                <div className="text-right">
                    <p className="text-sm text-gray-500">Total: {formatPrice(order.totalPrice)}</p>
                </div>
                <Link
                    href={`/order/${order.id}`}
                    className="px-4 py-2 text-sm font-medium text-blue-600 hover:text-blue-500 border border-blue-200 rounded-md hover:bg-blue-50 transition-colors"
                >
                    View Details
                </Link>
            </div>
        </div>
    );
}