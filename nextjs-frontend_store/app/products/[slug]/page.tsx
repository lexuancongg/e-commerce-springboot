'use client'
import {NextPage} from "next";
import {useProductDetailContext} from "@/context/productDetailContext";
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useState} from "react";
import {RatingVm} from "@/models/rating/RatingVm";


const ProductDetailPage :NextPage =   ()=>{
   const productDetailContextValue = useProductDetailContext();
   if (!productDetailContextValue) return ;
   const {productDetail ,productVariations ,productOptionValues}= productDetailContextValue;

    const navigationPaths : NavigationPathModel[] = [
        {
            pageName: 'Home',
            url: '/',
        },
        {
            pageName: 'products',
            url: '/',
        },
        {
            pageName: productDetail.name,
            url: '',
        },
    ]



    const [ratings , setRatings] = useState<RatingVm[]>([]);

    return (

    )
}
export default ProductDetailPage;