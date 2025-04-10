import {FC} from "react";
type Props = {
    productName: string,
    averageStar: number,
    totalRating : number
}
const ProductTitle : FC<Props> = (
    {
        totalRating,
        averageStar,
        productName
    }
)=>{
    return (
        <div className="product-detail-header">
            <div className="left">
                <h4 className="title">{productName}</h4>
                <div className="rating-star">
                    <Star star={averageStar}/>
                </div>
                <span className="rating-count">({totalRating})</span>
            </div>

            <span>
        <i className="bi bi-bag-heart fs-3 text-danger"></i>
      </span>
        </div>
    )


}
export default ProductTitle;