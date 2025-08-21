"use client"
import React, {useEffect, useRef, useState} from "react";
import productService from "@/services/product/productService";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";

export   function Slide(){
    const [featuredProduct,setFeaturedProduct] = useState<ProductPreviewVm[]>([]);
    // lấy theo kiểu tham chiếu tới trong dom
    const slideRef = useRef<HTMLDivElement>(null);
    const slide_items_refs = useRef<HTMLDivElement[]>([]);
    // get api lấy ngẫu nhiên 10sp noi bat lam slide
    useEffect(() => {
        productService.getFeaturedProductsMakeSlide()
            .then((responseProductPreviews)=>{
                setFeaturedProduct(responseProductPreviews);
            })
            .catch((error)=>{
                console.log('error' , error)
            })
    }, []);

    React.useEffect(() => {
        let i = 0;
        const idInterval = setInterval(() => {
            // trong dom thì mỗi ptu chỉ có một vị trí trong cây html , nếu append ptu đã tồn tại thì nó bị di chuyển xuống cuối thay vì đc nhân bản
            if(slideRef.current)  slideRef.current.append(slide_items_refs.current[i]);
            i = i == slide_items_refs.current.length -1 ? 0 : i +1 ;
        }, 2500);
        // khi unmouser
        return () => {
            clearInterval(idInterval);
        }
    }, [])


    return (
        <div className={"home-slide"}>
            <div ref={slideRef} className="slide">
                {featuredProduct && featuredProduct.map((product, index) => {
                    return (
                        // khi map qua từng ptu , react tạo ptu Dom (div) => gọi callback và truyền vao ptu mới tạo(div)
                        <div ref={(element)=>{
                            if(element) slide_items_refs.current[index] = element;
                        }} key={product.id} className="item" style={{backgroundImage: `url(${product.avatarUrl})`}}></div>
                    )
                })}
            </div>
        </div>
    )
}