import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";

export const product_variant_demo_data : ProductVariantVm[]=  [
    {
        id: 1,
        name: "T-Shirt Red - M",
        slug: "tshirt-red-m",
        sku: "TSHIRT-RED-M",
        gtin: "1234567890123",
        price: 199000,
        avatarUrl: {
            url: "https://example.com/images/red-m.jpg",
            id: 3
        },
        productImages: [
            { id: 101, url: "https://example.com/images/red-m-1.jpg" },
            { id: 102, url: "https://example.com/images/red-m-2.jpg" }
        ],
        optionValues: {
            1: "Red", // color
            2: "M"    // size
        }
    },
    {
        id: 2,
        name: "T-Shirt Blue - L",
        slug: "tshirt-blue-l",
        sku: "TSHIRT-BLUE-L",
        gtin: "1234567890124",
        price: 199000,
        avatarUrl: {
            url: "https://example.com/images/blue-l.jpg",
            id: 4
        },
        productImages: [
            { id: 103, url: "https://example.com/images/blue-l-1.jpg" }
        ],
        optionValues: {
            1: "Blue",
            2: "L"
        }
    }
];