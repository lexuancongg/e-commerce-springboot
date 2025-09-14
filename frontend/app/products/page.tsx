'use client'
import {useRouter, useSearchParams} from "next/navigation";
import {ChangeEvent, useEffect, useRef, useState} from "react";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {Container, Row} from "react-bootstrap";

import ReactPaginate from "react-paginate";
import {CategoryVm} from "@/models/category/CategoryVm";
import categoryService from "@/services/category/categoryService";
import * as querystring from "node:querystring";
import productService from "@/services/product/productService";
import FilterProduct from "@/components/product/FilterProduct";


const CATEGORY_SLUG = 'categorySlug';


const productsDemo : ProductPreviewVm[] = [
    {
        id: 1,
        name: "Women T-Shirt",
        price: 19.99,
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-01.jpg",
        slug: "women",
    },
    {
        id: 2,
        name: "Men Jacket",
        price: 49.99,
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-02.jpg",
        slug: "men",
    },
    {
        id: 3,
        name: "Leather Bag",
        price: 79.99,
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-03.jpg",
        slug: "bag",
    },
    {
        id: 4,
        name: "Running Shoes",
        price: 59.99,
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-04.jpg",
        slug: "shoes",
    },
    {
        id: 5,
        name: "Running Shoes",
        price: 59.99,
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-05.jpg",
        slug: "shoes",
    },
    {
        id: 6,
        name: "Running Shoes",
        price: 59.99,
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/product-06.jpg",
        slug: "shoes",
    },

];


const categoriesDemo : CategoryVm[] = [
    {
        id: 1,
        name: "Woment",
        slug: "electronics",

        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/banner-01.jpg",
    },
    {
        id: 2,
        name: "Men",
        slug: "fashion",
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/banner-02.jpg",
    },
    {
        id: 3,
        name: "Kids",
        slug: "fashion",
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/banner-03.jpg",
    },
    {
        id: 4,
        name: "Fashion",
        slug: "fashion",
        avatarUrl: "https://preview.colorlib.com/theme/cozastore/images/banner-05.jpg",
    }
]

export default function ProductList() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const [products, setProducts] = useState<ProductPreviewVm[]>(productsDemo);
    const [totalPage, setTotalPage] = useState<number>(0);
    const [pageIndex, setPageIndex] = useState<number>(0);
    const [categories, setCategories] = useState<CategoryVm[]>(categoriesDemo);
    const [filters, setFilters] = useState<any>(null);
    const [categoryIdActive, setCategoryIdActive] = useState<number>(0);
    const [isFilterOpen, setIsFilterOpen] = useState(false);

    const [isSearchOpen, setIsSearchOpen] = useState(false);


    useEffect(() => {
        categoryService.getCategories()
            .then((responseCategories) => {
                let categoryId: number = 0;
                if (Array.from(searchParams.entries()).length > 0 && searchParams.get(CATEGORY_SLUG)) {
                    const categorySlugValue: string = searchParams.get(CATEGORY_SLUG) as string;
                    categoryId = responseCategories.find(cate => cate.slug == categorySlugValue)?.id !;

                }
                if (categoryId)
                    setCategoryIdActive(categoryId);
                setCategories(responseCategories);
            })
    }, []);



    useEffect(() => {
        const paramsObj: Record<string, string> = {}
        if (Array.from(searchParams.entries()).length > 0) {
            for (const [key, value] of searchParams.entries()) {
                paramsObj[key] = value;
            }
        }
        setFilters(paramsObj);

    }, [searchParams.toString()])

    // khi filter thay đổi thì load lại product
    useEffect(() => {


        if (filters == null) {
            return;
        }

        let predicates = querystring.stringify({...filters, pageIndex: pageIndex});
        productService.getProductByMultiParams(predicates)
            .then(responseProductsPagingVm => {
                setProducts(responseProductsPagingVm.productPreviewsPayload);
                setTotalPage(responseProductsPagingVm.totalPages);
            })

    }, [filters])


    const updateFilter = (key: string, value: string | number) => {

        const currentValue = searchParams.get(key);
        if (currentValue && currentValue == value) {
            return;
        }
        pushParamsToRouter(key, value)
        setPageIndex(0)

    }
    const pushParamsToRouter = (key: string, value: string | number) => {
        const params = new URLSearchParams(searchParams.toString())
        params.set(key, String(value))
        router.push(`?${params.toString()}`)
    }







    const changePage = ({selected}: any) => {
        setPageIndex(selected);
        pushParamsToRouter("pageIndex", selected)
    }

    const handleDeleteFilter = (event: any) => {


    }




    return (
        <Container className="mt-16">
            <div className="mb-4">
                <h3 className="text-4xl font-bold text-gray-900">Product Overview</h3>
            </div>

            {/* Tabs */}
            <div className="flex items-center justify-between border-b pb-4 mb-8">
                <div className="flex space-x-8">

                    <button
                        onClick={() => setCategoryIdActive(0)}
                        className={`pb-2 border-b-2 transition-all duration-200 ${categoryIdActive === 0
                            ? 'text-red-800 font-medium border-red-500'
                            : 'text-gray-500 border-transparent hover:text-black hover:border-gray-300'
                        }`}
                    >
                        ALL
                    </button>
                    {categories.map((cat) => (
                        <button
                            key={cat.id}
                            onClick={() => setCategoryIdActive(cat.id)}
                            className={`pb-2 border-b-2 transition-all duration-200 ${categoryIdActive === cat.id
                                ? 'text-red-800 font-medium border-red-500'
                                : 'text-gray-500 border-transparent hover:text-black hover:border-gray-300'
                            }`}
                        >
                            {cat.name}
                        </button>
                    ))}
                </div>

                {/* Actions */}
                <div className="flex space-x-3">
                    <button
                        onClick={() => setIsFilterOpen(!isFilterOpen)}
                        className="flex items-center border px-4 py-2 rounded hover:bg-gray-100 text-sm"
                    >
                        <span className="material-icons mr-1 text-base">filter_list</span>
                        Filter
                    </button>
                    <button
                        onClick={() => setIsSearchOpen(!isSearchOpen)}
                        className="flex items-center border px-4 py-2 rounded hover:bg-gray-100 text-sm">
                        <span className="material-icons mr-1 text-base">search</span>
                        Search
                    </button>
                </div>
            </div>

            <div
                className={`transition-all duration-300 ease-in-out overflow-hidden ${
                    isSearchOpen ? 'max-h-[100px]' : 'max-h-0'
                }`}
            >
                <div className="bg-gray-50 p-4">
                    <div className="flex items-center">
                        <input
                            type="text"
                            placeholder="Search products..."
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        />
                        <button className="ml-2 px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">
                            Search
                        </button>
                    </div>
                </div>
            </div>


            {/* Filter Panel */}
            <FilterProduct isShow={isFilterOpen}></FilterProduct>

            {/* Products grid */}
            <div className="row isotope-grid gap-y-8 mt-8">
                {productsDemo.map((product) => (
                    <div
                        key={product.id}
                        className="col-sm-6 col-md-4 col-lg-3 p-b-35 isotope-item"
                    >
                        <div className="block2 group">
                            {/* Ảnh + overlay */}
                            <div className="relative overflow-hidden rounded-md">
                                <img
                                    src={product.avatarUrl}
                                    alt={product.name}
                                    className="w-full h-auto transition-transform duration-300 group-hover:scale-105"
                                />

                                {/* Overlay Quick View */}
                                <a
                                    href="#"
                                    className="absolute inset-0 flex items-center justify-center bg-black/40 opacity-0 group-hover:opacity-100 transition duration-300"
                                >
                                    <span className="text-white text-sm font-medium bg-red-500 px-4 py-2 rounded">
                                        Quick View
                                    </span>
                                </a>
                            </div>

                            {/* Info */}
                            <div className="mt-3 flex justify-between items-center">
                                <div>
                                    <a
                                        href=""
                                        className="block text-gray-700 font-medium hover:text-fuchsia-500 transition"
                                    >
                                        {product.name}
                                    </a>
                                    <span className="text-gray-900 font-semibold transition">
                                        ${product.price.toFixed(2)}
                                    </span>
                                </div>

                                <button className="text-gray-400 hover:text-red-500 transition">
                                    ♥
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>





            {totalPage > 1 && (
                <div className="mt-6">
                    <ReactPaginate
                        forcePage={pageIndex}
                        previousLabel={"←"}
                        nextLabel={"→"}
                        pageCount={totalPage}
                        onPageChange={changePage}
                        containerClassName="flex justify-center items-center space-x-2 mt-8"
                        pageClassName="px-3 py-1 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-100 transition"
                        activeClassName="!bg-black !text-white border-black"
                        previousClassName="px-3 py-1 border border-gray-300 rounded-md text-gray-500 hover:bg-gray-100"
                        nextClassName="px-3 py-1 border border-gray-300 rounded-md text-gray-500 hover:bg-gray-100"
                        disabledClassName="opacity-50 cursor-not-allowed"
                    />

                </div>
            )}
        </Container>


    );


}