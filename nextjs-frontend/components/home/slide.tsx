"use client"
import React, { useRef, useState} from "react";

export   function Slide(){
    const [featuredProduct,setFeaturedProduct] = useState([
        {
            id:1,
            url: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        },
        {
            id:2,
            url:'https://obs.line-scdn.net/0hYVn9GL_6BnlTQBPC0sJ5LmoWCghgJBN_PThKTyEUDUF_dl18aHUbA3MQW0FieEJ6aTpIHnFHX0B8eEJ8PC4',
        },
        {
            id:3,
            url: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        },
        {
            id:4,
            url: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        },
        {
            id:5,
            url: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        },
    ]);

    const slideRef = useRef<HTMLDivElement>(null);
    const slide_items_refs = useRef<HTMLDivElement[]>([]);

    React.useEffect(() => {
        let i = 0;
        const idInterval = setInterval(() => {
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
                        <div ref={(element)=>{
                            if(element) slide_items_refs.current[index] = element;
                        }} key={product.id} className="item" style={{backgroundImage: `url(${product.url})`}}></div>
                    )
                })}
            </div>
        </div>
    )
}