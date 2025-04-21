import {FC} from "react";
// lỗi vì thư viện là js thuần còn đây dùng ts => fix trong tsconfig
import StarRatings from 'react-star-ratings';


type Props = {
    averageStar: number
}

const Star:FC<Props> = ({averageStar})=>{
    return (
        <StarRatings
            rating={averageStar}
            starRatedColor="#fb6e2e"
            numberOfStars={5}
            starDimension="18px"
            starSpacing="0"
            name="rating-header"
        />
    )
}
export default Star;