'use client'
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useRouter} from "next/navigation";
import {ChangeEvent, useEffect, useRef, useState} from "react";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {Container, Row} from "react-bootstrap";
import Head from "next/head";
import NavigationComponent from "@/components/common/navigationComponent";
import ProductCard from "@/components/cart/productCard";
import ReactPaginate from "react-paginate";
import {CategoryVm} from "@/models/category/CategoryVm";
import categoryService from "@/services/category/categoryService";
import {usePathname, useSearchParams} from 'next/navigation'
import * as querystring from "node:querystring";
import productService from "@/services/product/productService";

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

export default function ProductList() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const [products, setProducts] = useState<ProductPreviewVm[]>([]);
    const [totalPage, setTotalPage] = useState<number>(1);
    const [pageIndex, setPageIndex] = useState<number>(0);
    const [categories, setCategories] = useState<CategoryVm[]>([]);
    const [filters, setFilters] = useState<any>(null);
    const inputSearchRef = useRef<HTMLInputElement>(null);
    const inputStartPriceRef = useRef<HTMLInputElement>(null);
    const inputEndPriceRef = useRef<HTMLInputElement>(null);
    const [categoryIdActive, setCategoryIdActive] = useState<number>(0);


    // khi nhung param query thay doi thi set lai page 0
    const updateFilter = (key: string, value: string | number) => {
        // trường hợp push key và value đã có => giá trị không thaydodoiri thì không set lại page
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

    useEffect(() => {
        // lấy ds category để lọc
        categoryService.getCategories()
            .then((responseCategories) => {
                let categoryId: number = 0;
                // check xem trên query url có param nào không
                if (Array.from(searchParams.entries()).length > 0 && searchParams.get(CATEGORY_SLUG)) {
                    const categorySlugValue: string = searchParams.get(CATEGORY_SLUG) as string;
                    categoryId = responseCategories.find(cate => cate.slug == categorySlugValue)?.id !;


                }
                if (categoryId) setCategoryIdActive(categoryId);
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

        // querystring : thư vện hổ trợ convert từ object sang query teen url
        let predicates = querystring.stringify({...filters, pageIndex: pageIndex});
        productService.getProductByMultiParams(predicates)
            .then(responseProductsPagingVm => {
                setProducts(responseProductsPagingVm.productPreviewsPayload);
                setTotalPage(responseProductsPagingVm.totalPages);
            })

    }, [filters])


    const changePage = ({selected}: any) => {
        setPageIndex(selected);
        pushParamsToRouter("pageIndex", selected)
    }

    const handleDeleteFilter = (event: any) => {
        setPageIndex(0);
        setCategoryIdActive(0)
        router.push('/products')
        if (inputSearchRef.current) {
            inputSearchRef.current.value = '';
        }
        if (inputStartPriceRef.current) {
            inputStartPriceRef.current.value = '';
        }
        if (inputEndPriceRef.current) {
            inputEndPriceRef.current.value = '';
        }

    }
    return (
        <Container>
            <Head>
                <title>listProduct</title>
            </Head>

            <NavigationComponent props={navigationPaths}></NavigationComponent>

            <div className="py-8 bg-gray-100">
                <div className="max-w-screen-xl mx-auto px-4">
                    <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
                        {/* Sidebar Filter */}
                        <div className="hidden lg:block bg-white p-6 rounded-lg shadow">
                            <div className="flex justify-between items-center mb-4">
                                <h3 className="text-lg font-semibold">Filter By</h3>
                                <button onClick={handleDeleteFilter}
                                        className="text-sm text-white bg-gray-600 px-3 py-1 rounded hover:bg-gray-700">
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
                                            className={`cursor-pointer px-3 py-1 border border-gray-500 rounded-full text-sm hover:bg-gray-200 
                                            ${categoryIdActive === cate.id ? 'bg-violet-500 text-white' : ''}`}
                                            onClick={() => {
                                                if (categoryIdActive != cate.id) {
                                                    updateFilter(CATEGORY_SLUG, cate.slug)
                                                }
                                                setCategoryIdActive(cate.id);
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
                                            ref={inputStartPriceRef}
                                            onChange={(event) => {
                                                updateFilter("startPrice", Number(event.target.value))
                                            }}
                                        />
                                    </div>
                                    <div className="w-1/2">
                                        <label className="block text-xs mb-1">To</label>
                                        <input
                                            type="number"
                                            className="w-full p-2 border rounded text-sm"
                                            placeholder="0"
                                            onChange={(event) => {

                                                updateFilter("endPrice", Number(event.target.value))
                                            }}
                                            ref={inputEndPriceRef}
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
                                        onChange={(event) => {
                                            updateFilter('productName', event.target.value)
                                        }}
                                        ref={inputSearchRef}
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
                                        onPageChange={changePage}
                                        containerClassName={'pagination-container'}
                                        previousClassName={'previous-btn'}
                                        nextClassName={'next-btn'}
                                        disabledClassName={'pagination-disabled'}
                                        activeClassName={'pagination-active'}
                                    />
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>


        </Container>
    );


}