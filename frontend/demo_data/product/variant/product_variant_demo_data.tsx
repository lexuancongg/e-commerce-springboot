import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";

export const product_variant_demo_data : ProductVariantVm[]=  [
    {
        id: 1,
        name: "T-Shirt Red - M",
        slug: "shirt-red-m",
        sku: "SHIRT-RED-M",
        gtin: "1234567890123",
        price: 199000,
        avatarUrl:"https://preview.colorlib.com/theme/cozastore/images/product-01.jpg",
        productImages: [
            { id: 101, url: "https://preview.colorlib.com/theme/cozastore/images/product-01.jpg" },
            { id: 102, url: "https://preview.colorlib.com/theme/cozastore/images/product-01.jpg" }
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
        avatarUrl:"https://preview.colorlib.com/theme/cozastore/images/product-02.jpg",
        productImages: [
            { id: 103, url: "https://preview.colorlib.com/theme/cozastore/images/product-02.jpg" },
            { id: 104, url: "https://preview.colorlib.com/theme/cozastore/images/product-02.jpg" }
        ],
        optionValues: {
            1: "Blue",
            2: "L"
        }
    }
];