'use client'
import {NextPage} from "next";
import {useState} from "react";
import {ProductPreviewVm} from "@/models/product/productPreviewVm";
import {Col, Container, Row} from "react-bootstrap";
import ProductCard from "@/components/cart/productCard";
import ReactPaginate from "react-paginate";


export const productss: ProductPreviewVm[] = [
    { id: 1, name: "Product 1", slug: "product-1", price: 100000, imageUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
    { id: 2, name: "Product 2", slug: "product-2", price: 150, imageUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
    { id: 3, name: "Product 3", slug: "product-3", price: 200, imageUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
    { id: 4, name: "Product 4", slug: "product-4", price: 250, imageUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
    { id: 5, name: "Product 5", slug: "product-5", price: 300, imageUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
];


const FeaturedProduct : NextPage = ()=>{
    const [products,setProducts] = useState<ProductPreviewVm[]>(productss);
    const [pageNo,setPageNo] = useState<number>(0);
    const [totalPage, setTotalPage] = useState<number>(5);

    const  changePage = ({selected}:any)=>{
        setPageNo(selected);
    }





    return (
        <Container className="featured-product-container">
            <div className='title'>Featured Products</div>
            <Row xs={5} xxl={6}>
                {products.length > 0 &&
                    products.map((product) => (
                        <Col key={product.id}>
                            <ProductCard product={product}></ProductCard>
                        </Col>
                    ))}
            </Row>
            {totalPage > 1 &&  (
                // phân trang
                <ReactPaginate
                    forcePage={pageNo}
                    previousLabel={'Previous'}
                    nextLabel={'Next'}
                    pageCount={totalPage}
                    onPageChange={changePage}
                    containerClassName={'pagination-container'}
                    previousClassName={'previous-btn'}
                    nextClassName={'next-btn'}
                    disabledClassName={'pagination-disabled'}
                    activeClassName={'pagination-active'}
                />
            )}

        </Container>
    )
}
export default FeaturedProduct;