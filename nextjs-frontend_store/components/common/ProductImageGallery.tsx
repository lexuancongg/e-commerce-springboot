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
            <Figure className={"transition-transform duration-300 w-full flex items-center justify-center"}>
                <LoadImageSafe src={productImageUrls.length ? productImageUrls[0] : ""}></LoadImageSafe>
            </Figure>
            <button disabled={currentIndex === 0} className="border-0 bg-none p-0 text-3xl text-gray-400"
                    onClick={() => {
                    }}>
                <i className="bi bi-chevron-left"></i>
            </button>
            <Figure>
                {productImageUrls.map(imageUrl => {
                    return (
                        <div>
                            <LoadImageSafe src={imageUrl}></LoadImageSafe>
                        </div>
                    )
                })}
            </Figure>
            <button
                disabled={currentIndex === productImageUrls.length - 1 || !images.length}
                className="slider-button"
                onClick={()=>{}}
            >
                <i className="bi bi-chevron-right"></i>
            </button>

        </>
    )

}