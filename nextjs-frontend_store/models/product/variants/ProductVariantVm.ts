import {ImageVm} from "@/models/image/imageVm";

export type ProductVariantVm = {
    id : number,
    name : string,
    slug: string,
    sku: string,
    gtin: string,
    price: number,
    avatarUrl: ImageVm,
    productImages: {
        id: number,
        url: string
    }[],
    optionValues:{
        [key:number] : string
    }



}