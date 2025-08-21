// OrderItemVm mock
import {CheckoutVm} from "@/models/order/checkout/CheckoutVm";
import {CheckoutItemVm} from "@/models/order/checkout/CheckoutItemVm";
import {OrderItemVm} from "@/models/order/OrderItemVm";

export const orderItems: OrderItemVm[] = [
    {
        id: 1,
        productId: 101,
        productName: "Áo Thun Nam Basic",
        quantity: 2,
        productPrice: 150000,
        totalPrice: 300000,
        productAvatarUrl: "https://example.com/images/product1.jpg"
    },
    {
        id: 2,
        productId: 102,
        productName: "Quần Jean Nữ",
        quantity: 1,
        productPrice: 400000,
        totalPrice: 400000,
        productAvatarUrl: "https://example.com/images/product2.jpg"
    }
];

export const checkoutItems: CheckoutItemVm[] = [
    {
        id: 1,
        productId: 101,
        productName: "Áo Thun Nam Basic",
        quantity: 2,
        productPrice: 150000,
    },
    {
        id: 2,
        productId: 102,
        productName: "Quần Jean Nữ",
        quantity: 1,
        productPrice: 400000,
    }
];

// CheckoutVm mock
export const  checkoutData: CheckoutVm = {
    id: 999,
    email: "khachhang@example.com",
    note: "Giao hàng giờ hành chính",
    totalAmount: 700000,
    checkoutItemVms: checkoutItems
};
