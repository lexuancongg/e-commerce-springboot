import {CartItemDetailVm} from "@/models/cart/CartItemDetailVm";
import {CartItemVm} from "@/models/cart/CartItemVm";

export const cartItems_detail_demo_data : CartItemDetailVm[] =
    [
        {
            productId: 101,
            quantity: 2,
            productName: "Product A",
            slug: "product-a",
            avatarUrl: "https://th.bing.com/th/id/OIP.VSnbm5qgNu_BDNvyBa0I5AHaHa?rs=1&pid=ImgDetMain",
            price: 199.99,
        },
        {
            productId: 102,
            quantity: 1,
            productName: "Product B",
            slug: "product-b",
            avatarUrl: "https://th.bing.com/th/id/OIP.VSnbm5qgNu_BDNvyBa0I5AHaHa?rs=1&pid=ImgDetMain",
            price: 299.99,
        },
        {
            productId: 103,
            quantity: 3,
            productName: "Product C",
            slug: "product-c",
            avatarUrl: "https://th.bing.com/th/id/OIP.VSnbm5qgNu_BDNvyBa0I5AHaHa?rs=1&pid=ImgDetMain",
            price: 149.99,
        },

    ];

export const cartItems_demo_data : CartItemVm[] = [
    { productId: 1, quantity: 2 },
    { productId: 2, quantity: 5 },
    { productId: 3, quantity: 1 },
    { productId: 4, quantity: 3 },
    { productId: 5, quantity: 4 },
];