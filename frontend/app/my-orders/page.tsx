'use client';
import { NextPage } from "next";
import { OrderStatus } from "@/models/order/OrderStatus";
import OrderStatusTab from "@/components/order/orderStatusTab";
import React, { useState } from "react";

const MyOrders: NextPage = () => {
    const [activeTab, setActiveTab] = useState<OrderStatus | null>(null);
    const orderStatus: string[] = Object.keys(OrderStatus);

    const TabButton = ({ status, isActive }: { status: OrderStatus | null; isActive: boolean }) => (
        <button
            onClick={() => setActiveTab(status)}
            className={`
                px-4 py-2 text-sm font-medium transition-colors duration-200 whitespace-nowrap
                ${isActive
                ? 'text-red-600 border-b-2 border-red-500'
                : 'text-gray-500 hover:text-gray-700 border-b-2 border-transparent hover:border-gray-300'
            }
            `}
        >
            {status || "ALL"}
        </button>
    );

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-6">My Orders</h1>

                {/* Tabs với horizontal scroll cho mobile */}
                <div className="relative mb-8">
                    <div className="flex overflow-x-auto space-x-1 pb-4 -mx-4 px-4 scrollbar-hide">
                        <TabButton status={null} isActive={activeTab === null} />
                        {orderStatus.map((status) => (
                            <TabButton
                                key={status}
                                status={status as OrderStatus}
                                isActive={activeTab === status}
                            />
                        ))}
                    </div>

                </div>

                <OrderStatusTab orderStatus={activeTab} />
            </div>
        </div>
    );
};

export default MyOrders;