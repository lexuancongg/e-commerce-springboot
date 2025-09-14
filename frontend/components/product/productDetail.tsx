"use client";

import { FC } from "react";
import { Container } from "react-bootstrap";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";

import { ProductDetailVm } from "@/models/product/productDetailVm";
import { ProductOptionValuesVm } from "@/models/product/options/ProductOptionValuesVm";
import { ProductVariantVm } from "@/models/product/variants/ProductVariantVm";

import ProductTitle from "@/components/product/productTitle";

type Props = {
    productDetail: ProductDetailVm;
    productOptionValues: ProductOptionValuesVm[];
    productVariations: ProductVariantVm[];
    averageStar: number;
    totalRating: number;
};

const ProductDetail: FC<Props> = ({
                                      productDetail,
                                      productOptionValues,
                                      productVariations,
                                      averageStar,
                                      totalRating,
                                  }) => {

    // console.log(productDetail.productImageUrls)
    return (
        <Container className="py-4">
            {/* Tiêu đề + rating */}
            <ProductTitle
                productName={productDetail.name}
                averageStar={averageStar}
                totalRating={totalRating}
            />

            <div className="row mt-4">
                {/* Left: Swiper hình ảnh */}
                <div className="col-md-6">
                    <Swiper spaceBetween={10} slidesPerView={1} loop>
                        {productDetail.productImageUrls.map((url, idx) => (
                            <SwiperSlide key={idx}>
                                <img
                                    src={url}
                                    alt={productDetail.name}
                                    className="img-fluid rounded shadow-sm"
                                />
                            </SwiperSlide>
                        ))}
                    </Swiper>
                </div>

                {/* Right: Thông tin sản phẩm */}
                <div className="col-md-6">
                    <h3 className="fw-bold text-danger mb-3">
                        {productDetail.price.toLocaleString()} đ
                    </h3>

                    {/* Options */}
                    {productOptionValues.map((option) => (
                        <div key={option.id} className="mb-3">
                            <strong>{option.name}: </strong>
                            {option.value.map((v) => (
                                <button
                                    key={v}
                                    className="btn btn-outline-secondary btn-sm mx-1"
                                >
                                    {v}
                                </button>
                            ))}
                        </div>
                    ))}

                    {/* Mô tả ngắn */}
                    <p className="mt-3">{productDetail.shortDescription}</p>

                    {/* Nút đặt hàng */}
                    <button className="btn btn-primary mt-3 px-4">Thêm vào giỏ</button>
                </div>
            </div>
        </Container>
    );
};

export default ProductDetail;
