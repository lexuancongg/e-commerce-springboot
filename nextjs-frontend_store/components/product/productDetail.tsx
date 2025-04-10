import {ProductDetailVm} from "@/models/product/productDetailVm";
import {ProductOptionValuesVm} from "@/models/product/options/ProductOptionValuesVm";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";
import {FC} from "react";

type Props = {
    productDetail: ProductDetailVm,
    productOptionValues: ProductOptionValuesVm,
    productVariations: ProductVariantVm,
    averageStar: number,
    totalRating: number
}

const ProductDetail: FC<Props> = ({
                                      productVariations,
                                      averageStar,
                                      productDetail,
                                      productOptionValues,
                                      totalRating
                                  }) => {


    return (
        <>

        </>
    )


}
export default ProductDetail;