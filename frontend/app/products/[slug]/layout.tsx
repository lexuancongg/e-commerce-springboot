import React from "react";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";
import productService from "@/services/product/productService";
import {ProductDetailVm} from "@/models/product/productDetailVm";
import {ProductOptionValuesVm} from "@/models/product/options/ProductOptionValuesVm";
import {ProductOptionValueVm} from "@/models/product/options/ProductOptionValueVm";
import ProductDetailProvider, {ProductDetailContext} from "@/context/productDetailContext";
import ClientProductDetailLayout from "@/components/product/ClientProductDetailLayout";


const fetchProductVariations = async (productId: number): Promise<ProductVariantVm[]> => {
    try {
        let productVariations = await productService.getProductVariationsByParentId(productId);
        if (productVariations.length > 0) {
            productVariations = productVariations.sort((p1, p2) => {
                return Object.keys(p1).length - Object.keys(p2).length;
            })
        }
        return productVariations;

    } catch (error) {
        console.log("error");
        return [];
    }
}
const fetchDetailProduct = async (slug: string): Promise<ProductDetailVm | null> => {
    try {

        return await productService.getDetailProductBySlug(slug)
    } catch (error) {
        console.log("error fetchDetailProduct", error)
        return null;

    }
}

// phải lấy trong optionCombinate vì thể hiện các variant cụ thể
const fetchProductOptionValues = async (productId: number): Promise<ProductOptionValueVm[]> => {
    try {
        return await productService.getProductOptionValues(productId);
    } catch (error) {
        console.log("error fetchProductOptionValues ", error)
        return [];
    }
}


// vì mặc định là server component nên có thể dùng async và fetch api bằng await trực tếp
export default async function ProductDetailLayout(
    {
        children,
        params
    }: {
        children: React.ReactNode,
        params: Promise<{
            slug: string
        }>
    }
) {
    const {slug} = await params;
    // call api trực tiếp trên server component
    const product = await fetchDetailProduct(slug as string);
    if (product == null) return;
    const options: ProductOptionValuesVm[] = [];
    let productVariations: ProductVariantVm[] = [];
    if (product.hasOptions) {
        const productOptionValues = await fetchProductOptionValues(product.id);
        for (const productOptionValue of productOptionValues) {
            const index = options.findIndex((option, index) => {
                return option.name == productOptionValue.optionName;
            })
            if (index > -1) {
                options[index].value.push(productOptionValue.value)
                continue
            }
            const newOption: ProductOptionValuesVm = {
                name: productOptionValue.optionName,
                id: productOptionValue.id,
                value: [productOptionValue.value]
            }
            options.push(newOption);
        }
        productVariations = await fetchProductVariations(product.id);
    }


    return (
        // đây là server component mà conentext na l client compoennt => trực tiếp => lôĩ
        // <ProductDetailContext.Provider
        //     value={{
        //         productDetail: product,
        //         productVariations: productVariations,
        //         productOptionValues: options
        //     }}
        // >
        //     {children}
        // </ProductDetailContext.Provider>
            // <ClientProductDetailLayout
            //     productDetail={product}
            //     productOptionValues={options}
            //     productVariations={productVariations}
            // >
            //     {children}
            // </ClientProductDetailLayout>
        <ProductDetailProvider value={{
            productDetail:product,
            productVariations:productVariations,
            productOptionValues:options
        }}>{children}</ProductDetailProvider>
    );






}