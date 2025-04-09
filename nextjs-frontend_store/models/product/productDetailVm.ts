import {AttributeGroupValueVm} from "@/models/product/attribute/AttributeGroupValueVm";

export  type  ProductDetailVm = {
    id: number,
    name: string,
    brandName: string,
    categories : string[],
    attributeGroupValues: AttributeGroupValueVm[],
    shortDescription: string,
    description: string,
    specifications: string,
    price : number,
    hasOptions : boolean,
    avatarUrl : string,
    isFeatured : boolean,
    isPublic: boolean,
    isOrderEnable  : boolean ,
    productImageUrls : string[]

}