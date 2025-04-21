'use client'
import {NextPage} from "next";
import {useProductDetailContext} from "@/context/productDetailContext";
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useState} from "react";
import {RatingVm} from "@/models/rating/RatingVm";
import {Container} from "react-bootstrap";
import NavigationComponent from "@/components/common/navigationComponent";
import ProductDetail from "@/components/product/productDetail";
import ProductAttribute from "@/components/product/ProductAttribute";
import ProductSpecificationAndReviews from "@/components/product/ProductSpecificationAndReviews";


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
    ];



    const [ratings , setRatings] = useState<RatingVm[]>([]);
    const [averageStar, setAverageStar] = useState<number>(4.6);
    const [totalRating, setTotalRating] = useState<number>(0);



    return (
        <Container>
            <NavigationComponent props={navigationPaths}></NavigationComponent>

            <ProductDetail
                productDetail={productDetail}
                productOptionValues={productOptionValues}
                productVariations={productVariations}
                averageStar={averageStar}
                totalRating={ratings.length}>
            </ProductDetail>
            <ProductAttribute productDetail={productDetail}>

            </ProductAttribute>
            <ProductSpecificationAndReviews
                productDetail={productDetail}
                totalRating={totalRating}>

            </ProductSpecificationAndReviews>


        </Container>
    )
}
export default ProductDetailPage;