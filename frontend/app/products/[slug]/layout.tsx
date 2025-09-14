import React from "react";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";
import productService from "@/services/product/productService";
import {ProductDetailVm} from "@/models/product/productDetailVm";
import {ProductOptionValuesVm} from "@/models/product/options/ProductOptionValuesVm";
import {ProductOptionValueVm} from "@/models/product/options/ProductOptionValueVm";
import ProductDetailProvider, {ProductDetailContext} from "@/context/productDetailContext";
import ClientProductDetailLayout from "@/components/product/ClientProductDetailLayout";
import {SpecificProductVariantVm} from "@/models/product/specific_variant/SpecificProductVariantGetVm";


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
        const  product  = await productService.getDetailProductBySlug(slug);
        return  product;
    } catch (error) {
        console.log("error fetchDetailProduct", error)
        return null;

    }
}

const getSpecificProductVariantsByProductId = async (productId: number): Promise<SpecificProductVariantVm[]> => {
    try {
        return await productService.getSpecificProductVariantsByProductId(productId);
    } catch (error) {
        console.log("error getSpecificProductVariantsByProductId ", error)
        return [];
    }
}


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

    const product = await fetchDetailProduct(slug as string);

    if(product == null){
        return ;
    }
    const productOptions: ProductOptionValuesVm[] = [];
    let productVariations: ProductVariantVm[] = [];
    if (product.hasOptions) {
        const specificProductVariants = await getSpecificProductVariantsByProductId(product.id);
        for (const specificProductVariant of specificProductVariants) {
            const index = productOptions.findIndex((option, index) => {
                return option.name == specificProductVariant.productOptionName;
            })
            if (index > -1) {
                productOptions[index].value.push(specificProductVariant.productOptionValue)
                continue
            }
            const newOption: ProductOptionValuesVm = {
                name: specificProductVariant.productOptionName,
                id: specificProductVariant.id,
                value: [specificProductVariant.productOptionValue]
            }
            productOptions.push(newOption);
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
            productOptionValues:productOptions
        }}>{children}</ProductDetailProvider>
    );






}