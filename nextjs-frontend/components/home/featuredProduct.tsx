'use client'
import {NextPage} from "next";
import {useState} from "react";
import {ProductPreview} from "@/models/product/productPreview";
import {Col, Container, Row} from "react-bootstrap";


export const productss: ProductPreview[] = [
    { id: 1, name: "Product 1", slug: "product-1", price: 100, imageUrl: "https://example.com/product1.jpg" },
    { id: 2, name: "Product 2", slug: "product-2", price: 150, imageUrl: "https://example.com/product2.jpg" },
    { id: 3, name: "Product 3", slug: "product-3", price: 200, imageUrl: "https://example.com/product3.jpg" },
    { id: 4, name: "Product 4", slug: "product-4", price: 250, imageUrl: "https://example.com/product4.jpg" },
    { id: 5, name: "Product 5", slug: "product-5", price: 300, imageUrl: "https://example.com/product5.jpg" },
    { id: 6, name: "Product 6", slug: "product-6", price: 350, imageUrl: "https://example.com/product6.jpg" },
    { id: 7, name: "Product 7", slug: "product-7", price: 400, imageUrl: "https://example.com/product7.jpg" },
    { id: 8, name: "Product 8", slug: "product-8", price: 450, imageUrl: "https://example.com/product8.jpg" },
    { id: 9, name: "Product 9", slug: "product-9", price: 500, imageUrl: "https://example.com/product9.jpg" },
    { id: 10, name: "Product 10", slug: "product-10", price: 550, imageUrl: "https://example.com/product10.jpg" }
];


const FeaturedProduct : NextPage = ()=>{
    const [products,setProducts] = useState<ProductPreview[]>(productss);
    const [pageNo,setPageNo] = useState<number>(0);

    return (
        <Container className="featured-product-container">
            <div className='title'>Featured Products</div>
            <Row xs={5} xxl={6}>
                {products.length > 0 &&
                    products.map((product) => (
                        <Col key={product.id}>

                        </Col>
                    ))}
            </Row>
        </Container>
    )
}
export default FeaturedProduct;