'use client'
import {NextPage} from "next";
import {useProductDetailContext} from "@/context/productDetailContext";
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useEffect, useState} from "react";
import {RatingVm} from "@/models/rating/RatingVm";
import {Container} from "react-bootstrap";
import NavigationComponent from "@/components/common/navigationComponent";
import ProductDetail from "@/components/product/productDetail";
import ProductAttribute from "@/components/product/ProductAttribute";
import ProductSpecificationAndReviews from "@/components/product/ProductSpecificationAndReviews";
import ratingService from "@/services/rating/RatingService";


const pageSize = 10;
const ProductDetailPage :NextPage =   ()=>{
   const productDetailContextValue = useProductDetailContext();
   if (!productDetailContextValue) return ;
   const {productDetail ,productVariations ,productOptionValues}= productDetailContextValue;
   const productId = productDetail.id;
   const  handleCreateRating = ()=>{

   }

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
    const [averageStar, setAverageStar] = useState<number>(4.6);
    const [totalRating, setTotalRating] = useState<number>(0);
    const [totalPageRating, setTotalPageRating] = useState<number>(1);
    const [pageIndexRating, setPageIndexRating] = useState<number>(0);

    useEffect(() => {
        ratingService.getAverageStarByProductId(productId)
            .then(responseAvgStar=>{
                setAverageStar(responseAvgStar);
            })
    }, []);

    useEffect(() => {
        ratingService.getRatingsByProductId(productId , pageIndexRating , pageSize)
            .then((responseRatingPaging)=>{
                setRatings(responseRatingPaging.ratingsPayload);
                setTotalPageRating(responseRatingPaging.totalPages)
                setTotalRating(responseRatingPaging.totalElements)

            })
    }, [pageIndexRating, productId]);
    const handlePageChange = ({selected}: any)=>{
        setPageIndexRating(selected)
    }


    return (
        <Container>
            <NavigationComponent props={navigationPaths}></NavigationComponent>

            <ProductDetail
                productDetail={productDetail}
                productOptionValues={productOptionValues}
                productVariations={productVariations}
                averageStar={averageStar}
                totalRating={totalRating}>
            </ProductDetail>
            <ProductAttribute productDetail={productDetail}>

            </ProductAttribute>
            <ProductSpecificationAndReviews
                productDetail={productDetail}
                totalRating={totalRating}
                handleCreateRating={handleCreateRating}
                totalPageRating={5}
                ratings={ratings}
                handlePageChange={handlePageChange}


            >

            </ProductSpecificationAndReviews>


        </Container>
    )
}
export default ProductDetailPage;