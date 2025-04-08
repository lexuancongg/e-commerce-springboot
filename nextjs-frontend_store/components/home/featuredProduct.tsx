'use client'
import {NextPage} from "next";
import {useEffect, useState} from "react";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {Col, Container, Row} from "react-bootstrap";
import ProductCard from "@/components/cart/productCard";
import ReactPaginate from "react-paginate";
import categoryService from "@/services/category/categoryService";
import productService from "@/services/product/productService";





const FeaturedProduct : NextPage = ()=>{
    const [products,setProducts] = useState<ProductPreviewVm[]>([]);
    const [pageIndex,setPageIndex] = useState<number>(0);
    const [totalPage, setTotalPage] = useState<number>(1);

    const  changePage = ({selected}:any)=>{
        setPageIndex(selected);
    }

    // fetch api
    useEffect(() => {
        productService.getFeaturedProductsPaging(pageIndex)
            .then((responseFeaturedProductsPaging)=>{
                setProducts(responseFeaturedProductsPaging.productPreviewsPayload)
                setTotalPage(responseFeaturedProductsPaging.totalPages);
            })
    }, []);





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
                    forcePage={pageIndex}
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