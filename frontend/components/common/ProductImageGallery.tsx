import {useState} from "react";
import {Figure} from "react-bootstrap";
import LoadImageSafe from "@/components/common/loadImageSafe";
import {images} from "next/dist/build/webpack/config/blocks/images";

type Props = {
    productImageUrls:string[]

}
const NUMBER_SLIDE_IMAGE = 3;
export default function  ProductImageGallery({productImageUrls}:Props){
    const [currentIndex,setCurrentIndex] = useState<number>(0);
    const startSlideIndex = 0;


    return (
        <>
            {/* ảnh chinhs */}
            <div className={"transition-transform duration-300 w-full flex items-center justify-center"}>
                <LoadImageSafe src={productImageUrls.length ? productImageUrls[0] : ""}></LoadImageSafe>
            </div>
            {/* danh sách ảnh con*/}
            <div className={'flex  w-full items-center justify-between mt-4 '}>
                <button disabled={currentIndex === 0} className="border-0 bg-none p-0 text-3xl text-gray-400"
                        onClick={() => {
                        }}>
                    <i className="bi bi-chevron-left"></i>
                </button>
                <div className={'flex '}>
                    {productImageUrls.map(imageUrl => {
                        return (
                            <div className={productImageUrls[currentIndex] == imageUrl ? 'border-black' : ''}>
                                <LoadImageSafe width={100} height={100} src={imageUrl}></LoadImageSafe>
                            </div>
                        )
                    })}
                </div>
                <button
                    disabled={currentIndex === productImageUrls.length - 1 || !productImageUrls.length}
                    className="border-0 bg-none p-0 text-3xl text-gray-400"
                    onClick={() => {
                    }}
                >
                    <i className="bi bi-chevron-right"></i>
                </button>
            </div>

        </>
    )

}