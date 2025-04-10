import {FC} from "react";
import Star from "@/components/star/star";
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
        <div >
            <div>
                <h4 >{productName}</h4>
                <div >
                    <Star averageStar={averageStar}/>
                </div>
                <span >({totalRating})</span>
            </div>

            <span>
        <i className="bi bi-bag-heart fs-3 text-danger"></i>
      </span>
        </div>
    )


}
export default ProductTitle;