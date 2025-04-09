import React from "react";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";
import productService from "@/services/product/productService";
import {ProductDetailVm} from "@/models/product/productDetailVm";
import {ProductOptionVm} from "@/models/product/options/ProductOptionVm";


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
const fetchDetailProduct = async (slug: string) : Promise<ProductDetailVm|null> =>{
    try {
        return  await productService.getDetailProductBySlug(slug);

    }catch (error){
        console.log("error")
        return null;

    }
}

const fetchProductOptionValues = async (productId : number):Promise<Product>=>{

}


// vì mặc định là server component nên có thể dùng async và fetch api bằng await trực tếp
export default async function ProductDetailLayout(
    {
        children,
        params:{slug}
    }: {
        children: React.ReactNode ,
        params:{
            slug: string
        }
    }
) {
    // call api trực tiếp trên server component
    const product = await fetchDetailProduct(slug as string);
    if(product ==null) return ;
    const productOptions : ProductOptionVm[]= [];
    const productVariations : ProductVariantVm[] = [];
    if(product.hasOptions) {
        const productOptionValues =

    }


    return (
        <div>{slug}</div>
    )
}