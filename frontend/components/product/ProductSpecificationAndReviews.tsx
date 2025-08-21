import {ProductDetailVm} from "@/models/product/productDetailVm";
import {FC, useState} from "react";
import {Tab, Tabs} from "react-bootstrap";
import TabRating from "@/components/rating/TabRating";
import {RatingVm} from "@/models/rating/RatingVm";
import FormPostRating from "@/components/rating/FormPostRating";
import ListRating from "@/components/rating/ListRating";

type Props = {
    productDetail:ProductDetailVm,
    totalRating: number,
    handleCreateRating : ()=> void,
    handlePageChange:({selected}: any)=>void,
    ratings: RatingVm[],
    totalPageRating: number

}
const ProductSpecificationAndReviews : FC<Props>=(
    {
        productDetail ,
        totalRating,
        handleCreateRating,
        handlePageChange,
        ratings,
        totalPageRating
    }
)=>{


    return (
      <div className={'container  mt-4'}>
          <Tabs defaultActiveKey="Specification" id="product-detail-tab" className="mb-3 " fill>
              <Tab eventKey="Specification" title="Specification" style={{ minHeight: '200px' }}>
                  <div className="tabs" dangerouslySetInnerHTML={{ __html: productDetail.specifications }}></div>
              </Tab>
              <Tab eventKey="Reviews" title={`Reviews (${totalRating})`} style={{minHeight: '200px'}}>
                  <div>
                      <div
                          style={{
                              borderBottom: '1px solid lightgray',
                              marginBottom: 30,
                          }}
                      >
                          <FormPostRating
                              handleCreateRating={handleCreateRating}
                          />
                      </div>
                      <div>
                          <ListRating
                              totalRating={totalRating}
                              ratings={ratings}
                              handlePageChange={handlePageChange}
                              totalPage={totalPageRating}

                          />
                      </div>
                  </div>
              </Tab>
          </Tabs>
      </div>
    )
}
export default ProductSpecificationAndReviews;