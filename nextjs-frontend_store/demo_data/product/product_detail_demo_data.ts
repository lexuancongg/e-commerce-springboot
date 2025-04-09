import {ProductDetailVm} from "@/models/product/productDetailVm";

export const Product_detail_demo_data : ProductDetailVm =
    {
        id: 1,
        name: "Áo thun unisex basic",
        brandName: "Coolmate",
        categories: ["Thời trang", "Áo thun"],
        attributeGroupValues: [
            {
                name: " kỹ thuật ",
                attributeValues: [
                    { name: "vãi", value: "vãi cocon" },
                    { name: "chỉ", value: "chỉ vàng cocon " },
                ],
            },
            {
                name: "hình dáng",
                attributeValues: [
                    { name: "tay áo", value: "tay áo hình chữ M" },
                    { name: "cổ áo", value: "cổ tròn " },
                ],
            },
        ],
        shortDescription: "Áo thun unisex thoáng mát, dễ phối đồ.",
        description: "Chất liệu cotton 100%, phù hợp với thời tiết nóng ẩm. Kiểu dáng trẻ trung, form rộng.",
        specifications: "Chất liệu: Cotton | Cân nặng mẫu mặc: 70kg | Size M",
        price: 199000,
        hasOptions: true,
        avatarUrl: {
            id: 101,
            url: "https://example.com/avatar.jpg",
        },
        isFeatured: true,
        isPublic: true,
        isOrderEnable: true,
        productImageUrls: [
            { id: 201, url: "https://example.com/image1.jpg" },
            { id: 202, url: "https://example.com/image2.jpg" },
        ],
    };