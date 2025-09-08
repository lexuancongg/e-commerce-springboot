import {ProductOptionValueVm} from "@/models/product/options/ProductOptionValueVm";

export type CartItemDetailVm = {
    productId:number;
    quantity:number;
    productName: string;
    slug: string;
    imageUrl: string;
    price: number;
    productOptions: ProductOptionValueVm[]
}

