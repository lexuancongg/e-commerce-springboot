'use client'
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useRouter} from "next/navigation";
import {ChangeEvent, useEffect, useState} from "react";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {Container, Row} from "react-bootstrap";
import Head from "next/head";
import NavigationComponent from "@/components/common/navigationComponent";
import ProductCard from "@/components/cart/productCard";
import ReactPaginate from "react-paginate";
import {CategoryVm} from "@/models/category/CategoryVm";
import categoryService from "@/services/category/categoryService";
import { usePathname, useSearchParams } from 'next/navigation'
import Link from "next/link";
import {URLSearchParamsIterator} from "url";
import * as querystring from "node:querystring";
const navigationPaths: NavigationPathModel[] = [
    {
        pageName: "Home",
        url: '/'
    },
    {
        pageName: 'products',
        url: '#'
    }
]
const CATEGORY_SLUG = 'categorySlug';

export default function ProductList(){
    const router = useRouter();
    const pathname = usePathname();
    const searchParams = useSearchParams();
    const [products, setProducts] = useState<ProductPreviewVm[]>([]);
    const [totalPage , setTotalPage] = useState<number>(1);
    const [pageIndex,setPageIndex] = useState<number>(0);
    const [categories ,setCategories] = useState<CategoryVm[]>([]);
    const [filters , setFilters] = useState<any>(null);

    const handleFilter = ()=>{

    }

    useEffect(() => {
        // check xem trên query url có param nào không
        if(Array.from(searchParams.entries()).length > 0 && searchParams.get(CATEGORY_SLUG)){
            const categorySlugValue : string = searchParams.get(CATEGORY_SLUG) as string;


        }
        // lấy ds category để lọc
        categoryService.getCategories()
            .then((responseCategories)=>{
                setCategories(responseCategories);
            })
    }, []);


    useEffect(() => {
        if(Array.from(searchParams.entries()).length > 0){
            setPageIndex(0)
        }
        const paramsObj : Record<string, string> = {}
        for(const [key,value] of searchParams.entries()){
            paramsObj[key] = value;
        }
        setFilters(paramsObj);

    },[searchParams.entries()])

    // khi filter thay đổi thì load lại product
    useEffect(()=>{
        if(filters == null){
            return;
        }
        let predicates = querystring.stringify({ ...filters, pageNo: pageNo });


    },[filters])


    return (
        <Container>
            <Head>
                <title>listProduct</title>
            </Head>
            <Link href={'/products?bu=5'}>click</Link>

            <NavigationComponent props={navigationPaths}></NavigationComponent>
            <div className="py-8 bg-gray-100">
                <div className="max-w-screen-xl mx-auto px-4">
                    <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
                        {/* Sidebar Filter */}
                        <div className="hidden lg:block bg-white p-6 rounded-lg shadow">
                            <div className="flex justify-between items-center mb-4">
                                <h3 className="text-lg font-semibold">Filter By</h3>
                                <button className="text-sm text-white bg-gray-600 px-3 py-1 rounded hover:bg-gray-700">
                                    Clear filter
                                </button>
                            </div>

                            {/* Category */}
                            <div className="mb-6">
                                <h4 className="text-md font-semibold mb-2">Categories</h4>
                                <ul className="flex flex-wrap gap-2">
                                    {categories.map((cate) => (
                                        <li
                                            key={cate.id}
                                            className="cursor-pointer px-3 py-1 border border-gray-500 rounded-full text-sm hover:bg-gray-200"
                                            onClick={() => {
                                            }}
                                        >
                                            {cate.name}
                                        </li>
                                    ))}
                                </ul>
                            </div>

                            {/* Price Filter */}
                            <div>
                                <h4 className="text-md font-semibold mb-2">Price</h4>
                                <div className="flex gap-2">
                                    <div className="w-1/2">
                                        <label className="block text-xs mb-1">From</label>
                                        <input
                                            type="number"
                                            className="w-full p-2 border rounded text-sm"
                                            placeholder="0"
                                            onChange={(e) => {
                                            }}
                                        />
                                    </div>
                                    <div className="w-1/2">
                                        <label className="block text-xs mb-1">To</label>
                                        <input
                                            type="number"
                                            className="w-full p-2 border rounded text-sm"
                                            placeholder="0"
                                            onChange={(e) => {
                                            }}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* Product Section */}
                        <div className="lg:col-span-3">
                            <div
                                className="bg-white p-4 rounded-lg shadow mb-4 flex flex-col md:flex-row justify-between items-center gap-4">
                                <div className="flex items-center gap-3">
                                    <span className="text-sm font-medium">Sort By:</span>
                                    <select className="border p-2 rounded text-sm">
                                        <option value="">Featured</option>
                                        <option value="">Best Selling</option>
                                    </select>
                                </div>
                                <div className="w-full md:w-1/3">
                                    <input
                                        type="text"
                                        className="w-full border p-2 rounded text-sm"
                                        placeholder="Search..."
                                        onChange={(e) => {
                                        }}
                                    />
                                </div>
                            </div>

                            {/* Product Grid */}
                            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">
                                {products.length > 0 &&
                                    products.map((product) => (
                                        <ProductCard
                                            className={['products-page']}
                                            product={product}
                                            key={product.id}
                                        />
                                    ))}
                            </div>

                            {/* Pagination */}
                            {totalPage > 1 && (
                                <div className="mt-6">
                                    <ReactPaginate
                                        forcePage={pageIndex}
                                        previousLabel={'Previous'}
                                        nextLabel={'Next'}
                                        pageCount={totalPage}
                                        // onPageChange={changePage}
                                        containerClassName={'flex gap-2 justify-center mt-4'}
                                        previousClassName={'px-3 py-1 border rounded hover:bg-gray-200'}
                                        nextClassName={'px-3 py-1 border rounded hover:bg-gray-200'}
                                        disabledClassName={'opacity-50 pointer-events-none'}
                                        activeClassName={'bg-blue-500 text-white'}
                                    />
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>


        </Container>
    )


}