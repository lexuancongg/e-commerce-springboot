'use client'
import {createContext, useContext} from "react";
import {ProductDetailVm} from "@/models/product/productDetailVm";
import {ProductOptionValuesVm} from "@/models/product/options/ProductOptionValuesVm";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";

type ProductContextType = {
    productDetail: ProductDetailVm;
    productOptionValues: ProductOptionValuesVm[];
    productVariations: ProductVariantVm[];
};

const productDetailContext = createContext<ProductContextType | null>(null);

export const useProductDetailContext = () => {
    return useContext(productDetailContext)
}

export default productDetailContext;