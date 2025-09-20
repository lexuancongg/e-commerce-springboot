"use client";

import {FC, useState} from "react";
import {Swiper, SwiperSlide} from "swiper/react";
import {Navigation, Thumbs} from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/thumbs";

// Icons
import {FaStar, FaRegStar, FaHeart, FaShoppingCart, FaMinus, FaPlus} from "react-icons/fa";
import {IoIosArrowBack, IoIosArrowForward} from "react-icons/io";

import {ProductDetailVm} from "@/models/product/productDetailVm";
import {ProductOptionValuesVm} from "@/models/product/options/ProductOptionValuesVm";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";

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
    const [thumbsSwiper, setThumbsSwiper] = useState<any>(null);
    const [quantity, setQuantity] = useState(1);



    return (
        <div className="mt-md-5">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 lg:gap-12">
                    <div className="space-y-4 lg:space-y-6">
                        <div className="relative group">
                            <Swiper
                                modules={[Navigation, Thumbs]}
                                thumbs={{swiper: thumbsSwiper}}
                                spaceBetween={10}
                                slidesPerView={1}
                                loop={true}
                                navigation={{
                                    nextEl: ".swiper-button-next-custom",
                                    prevEl: ".swiper-button-prev-custom",
                                }}
                                className="h-96 lg:h-[500px] rounded-2xl overflow-hidden shadow-xl"
                            >
                                {productDetail.productImageUrls.map((url, idx) => (
                                    <SwiperSlide key={idx}>
                                        <div
                                            className="h-full bg-gradient-to-br from-gray-100 to-gray-200 flex items-center justify-center">
                                            <img
                                                src={url}
                                                alt={productDetail.name}
                                                className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
                                                loading="lazy"
                                            />
                                        </div>
                                    </SwiperSlide>
                                ))}
                            </Swiper>

                            <button
                                className="swiper-button-prev-custom absolute -left-3 top-1/2 -translate-y-1/2 z-10 w-10 h-10 bg-white/90 hover:bg-white rounded-full shadow-lg border border-gray-200 flex items-center justify-center text-gray-600 hover:text-gray-800 transition-all duration-200 transform -translate-x-2 lg:translate-x-0 group-hover:translate-x-0">
                                <IoIosArrowBack size={18}/>
                            </button>
                            <button
                                className="swiper-button-next-custom absolute -right-3 top-1/2 -translate-y-1/2 z-10 w-10 h-10 bg-white/90 hover:bg-white rounded-full shadow-lg border border-gray-200 flex items-center justify-center text-gray-600 hover:text-gray-800 transition-all duration-200 transform translate-x-2 lg:translate-x-0 group-hover:translate-x-0">
                                <IoIosArrowForward size={18}/>
                            </button>

                        </div>

                        <div className="relative">
                            <Swiper
                                onSwiper={setThumbsSwiper}
                                modules={[Navigation, Thumbs]}
                                spaceBetween={8}
                                slidesPerView="auto"
                                watchSlidesProgress={true}
                                breakpoints={{
                                    320: {slidesPerView: 3, spaceBetween: 8},
                                    768: {slidesPerView: 4, spaceBetween: 12},
                                    1024: {slidesPerView: 5, spaceBetween: 16},
                                }}
                                className="py-2"
                            >
                                {productDetail.productImageUrls.map((url, idx) => (
                                    <SwiperSlide key={idx} className="!w-20 lg:!w-24 px-1">
                                        <div
                                            className="h-16 lg:h-20 rounded-lg overflow-hidden border-2 border-transparent hover:border-blue-500 cursor-pointer transition-all duration-200 hover:shadow-md hover:-translate-y-1">
                                            <img
                                                src={url}
                                                alt={`${productDetail.name} - ${idx + 1}`}
                                                className="w-full h-full object-cover"
                                                loading="lazy"
                                            />
                                        </div>
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                        </div>
                    </div>


                    {/* product infor */}
                    <div className="space-y-4 lg:pl-8">
                        {/* Product Title & Rating */}
                        <div>
                            <h1 className="text-2xl lg:text-3xl font-bold text-gray-900 mb-2">
                                {productDetail.name}
                            </h1>
                            <div className="flex items-center gap-2 text-sm text-gray-500">
                                <div className="flex gap-1">
                                    {/* TODO: star rating component */}
                                </div>
                                <span>({totalRating} đánh giá)</span>
                            </div>
                        </div>

                        {/* Price */}
                        <div>
        <span className="text-3xl lg:text-4xl font-bold text-red-500">
          {productDetail.price.toLocaleString()} đ

        </span>
                        </div>

                        {/* Product Options */}
                        <div className="space-y-3">
                            {productOptionValues.map((option) => (
                                <div key={option.id}>
                                    <label className="block text-sm font-semibold text-gray-700 mb-2 capitalize">
                                        {option.name.toLowerCase()}
                                    </label>
                                    <div className="flex flex-wrap gap-2">
                                        {option.value.map((v) => (
                                            <button
                                                key={v}
                                                className={`px-4 py-2 rounded-lg font-medium border border-gray-300 transition `}>
                                                {v}
                                            </button>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>

                        {/* Quantity Selector */}
                        <div>
                            <label className="block text-sm font-semibold text-gray-700 mb-2">
                                Số lượng
                            </label>
                            <div className="flex items-center gap-3">
                                <button
                                    className="w-10 h-10 rounded-full border border-gray-300 flex items-center justify-center text-gray-600 hover:text-gray-800 hover:border-gray-400 transition"
                                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                                >
                                    -
                                </button>
                                <span
                                    className="w-12 text-center text-lg font-semibold text-gray-700 bg-gray-100 rounded-lg py-1">
            {quantity}
          </span>
                                <button
                                    className="w-10 h-10 rounded-full border border-gray-300 flex items-center justify-center text-gray-600 hover:text-gray-800 hover:border-gray-400 transition"
                                    onClick={() => setQuantity(quantity + 1)}
                                >
                                    +
                                </button>
                            </div>
                        </div>

                        <div className="flex flex-col sm:flex-row gap-3">
                            <button
                                className="flex-1 flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 text-white py-3 px-5 rounded-xl font-semibold shadow transition">
                                <FaShoppingCart size={20}/>
                                Thêm vào giỏ
                            </button>
                            <button
                                className="flex-1 bg-green-600 hover:bg-green-700 text-white py-3 px-5 rounded-xl font-semibold shadow transition">
                                Mua ngay
                            </button>
                        </div>
                    </div>
                </div>
            </div>


        </div>
    );
};

export default ProductDetail;