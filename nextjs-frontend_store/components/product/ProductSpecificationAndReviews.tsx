import {ProductDetailVm} from "@/models/product/productDetailVm";
import {FC, useState} from "react";
import {Tab, Tabs} from "react-bootstrap";
import TabRating from "@/components/rating/TabRating";

type Props = {
    productDetail:ProductDetailVm,
    totalRating: number,
    handleCreateRating : ()=> void

}
const ProductSpecificationAndReviews : FC<Props>=(
    {
        productDetail ,
        totalRating,
        handleCreateRating
    }
)=>{


    return (
        <Tabs defaultActiveKey="Specification" id="product-detail-tab" className="mb-3 " fill>
            <Tab eventKey="Specification" title="Specification" style={{ minHeight: '200px' }}>
                <div className="tabs" dangerouslySetInnerHTML={{ __html: productDetail.specifications }}></div>
            </Tab>
            <TabRating
                handleCreateRating={handleCreateRating}
                totalRating={totalRating}
            >

            </TabRating>
        </Tabs>
    )
}
export default ProductSpecificationAndReviews;