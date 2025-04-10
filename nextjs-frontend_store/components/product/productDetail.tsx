import {ProductDetailVm} from "@/models/product/productDetailVm";
import {ProductOptionValuesVm} from "@/models/product/options/ProductOptionValuesVm";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";
import {FC} from "react";
import {Container} from "react-bootstrap";
import NavigationComponent from "@/components/common/navigationComponent";
import ProductTitle from "@/components/product/productTitle";
import ProductImageGallery from "@/components/common/ProductImageGallery";

type Props = {
    productDetail: ProductDetailVm,
    productOptionValues: ProductOptionValuesVm[],
    productVariations: ProductVariantVm[],
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
            <ProductTitle
                productName={productDetail.name} averageStar={averageStar} totalRating={totalRating}
            ></ProductTitle>
            {/* chia ra hai phan*/}
            <div className={"row justify-content-center"}>
                <div className={"col-6"}>
                {/*    thông tin hinh ảnh*/}
                    <ProductImageGallery productImageUrls={["bu","bu","bu","bu"]}></ProductImageGallery>
                </div>

            </div>

        </>
    )


}
export default ProductDetail;