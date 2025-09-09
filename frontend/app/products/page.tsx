'use client'
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useRouter} from "next/navigation";
import {ChangeEvent, useEffect, useRef, useState} from "react";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {Container, Row} from "react-bootstrap";
import Head from "next/head";
import NavigationComponent from "@/components/common/navigationComponent";
import ProductCard from "@/components/product/productCard";
import ReactPaginate from "react-paginate";
import {CategoryVm} from "@/models/category/CategoryVm";
import categoryService from "@/services/category/categoryService";
import {usePathname, useSearchParams} from 'next/navigation'
import * as querystring from "node:querystring";
import productService from "@/services/product/productService";


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


    const productsDemo = [
        {
            id: "1",
            name: "Women T-Shirt",
            price: 19.99,
            image: "https://preview.colorlib.com/theme/cozastore/images/product-01.jpg",
            category: "women",
        },
        {
            id: "2",
            name: "Men Jacket",
            price: 49.99,
            image: "https://preview.colorlib.com/theme/cozastore/images/product-02.jpg",
            category: "men",
        },
        {
            id: "3",
            name: "Leather Bag",
            price: 79.99,
            image: "https://preview.colorlib.com/theme/cozastore/images/product-03.jpg",
            category: "bag",
        },
        {
            id: "4",
            name: "Running Shoes",
            price: 59.99,
            image: "https://preview.colorlib.com/theme/cozastore/images/product-04.jpg",
            category: "shoes",
        },
        {
            id: "5",
            name: "Running Shoes",
            price: 59.99,
            image: "https://preview.colorlib.com/theme/cozastore/images/product-05.jpg",
            category: "shoes",
        },
        {
            id: "6",
            name: "Running Shoes",
            price: 59.99,
            image: "https://preview.colorlib.com/theme/cozastore/images/product-06.jpg",
            category: "shoes",
        },

    ];
    const [activeTab, setActiveTab] = useState("All Products");


    const categoriesDemo = ["All Products", "Women", "Men", "Bag", "Shoes", "Watches"];
    const [isFilterOpen, setIsFilterOpen] = useState(false);

    const [isSearchOpen, setIsSearchOpen] = useState(false);
    return (
        <Container className="mt-16">
            <div className="mb-4">
                <h3 className="text-4xl font-bold text-gray-900">Product Overview</h3>
            </div>

            {/* Tabs */}
            <div className="flex items-center justify-between border-b pb-4 mb-8">
                <div className="flex space-x-8">
                    {categoriesDemo.map((cat) => (
                        <button
                            key={cat}
                            onClick={() => setActiveTab(cat)}
                            className={`pb-2 border-b-2 transition-all duration-200 ${activeTab === cat
                                ? 'text-black font-medium border-red-500'
                                : 'text-gray-500 border-transparent hover:text-black hover:border-gray-300'
                            }`}
                        >
                            {cat}
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
                        onClick={()=> setIsSearchOpen(!isSearchOpen)}
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
            <div
                className={`transition-all duration-300 ease-in-out overflow-hidden ${
                    isFilterOpen ? 'max-h-[300px]' : 'max-h-0'
                }`}
            >
                <div className="bg-red-50 p-4 grid grid-cols-4 gap-4">
                    {/* Sort By */}
                    <div>
                        <h4 className="text-sm font-medium text-gray-700 mb-2">Sort By</h4>
                        <ul className="space-y-1">
                            <li className="text-sm text-gray-600 hover:text-black">Default</li>
                            <li className="text-sm text-gray-600 hover:text-black">Popularity</li>
                            <li className="text-sm text-gray-600 hover:text-black">Average rating</li>
                            <li className="text-sm text-gray-600 hover:text-black">Newness</li>
                            <li className="text-sm text-gray-600 hover:text-black">Price: Low to High</li>
                            <li className="text-sm text-gray-600 hover:text-black">Price: High to Low</li>
                        </ul>
                    </div>

                    {/* Price */}
                    <div>
                        <h4 className="text-sm font-medium text-gray-700 mb-2">Price</h4>
                        <ul className="space-y-1">
                            <li className="text-sm text-gray-600 hover:text-black">All</li>
                            <li className="text-sm text-gray-600 hover:text-black">$0.00 - $50.00</li>
                            <li className="text-sm text-gray-600 hover:text-black">$50.00 - $100.00</li>
                            <li className="text-sm text-gray-600 hover:text-black">$100.00 - $150.00</li>
                            <li className="text-sm text-gray-600 hover:text-black">$150.00 - $200.00</li>
                            <li className="text-sm text-gray-600 hover:text-black">$200.00+</li>
                        </ul>
                    </div>

                    {/* Color */}
                    <div>
                        <h4 className="text-sm font-medium text-gray-700 mb-2">Color</h4>
                        <ul className="space-y-1">
                            <li className="flex items-center">
                                <span className="w-4 h-4 bg-black rounded-full mr-2"></span> Black
                            </li>
                            <li className="flex items-center">
                                <span className="w-4 h-4 bg-blue-500 rounded-full mr-2"></span> Blue
                            </li>
                            <li className="flex items-center">
                                <span className="w-4 h-4 bg-gray-400 rounded-full mr-2"></span> Grey
                            </li>
                            <li className="flex items-center">
                                <span className="w-4 h-4 bg-green-500 rounded-full mr-2"></span> Green
                            </li>
                            <li className="flex items-center">
                                <span className="w-4 h-4 bg-red-500 rounded-full mr-2"></span> Red
                            </li>
                            <li className="flex items-center">
                                <span className="w-4 h-4 bg-white border rounded-full mr-2"></span> White
                            </li>
                        </ul>
                    </div>

                    {/* Tags */}
                    <div>
                        <h4 className="text-sm font-medium text-gray-700 mb-2">Tags</h4>
                        <ul className="space-y-1">
                            <li className="text-sm text-gray-600 hover:text-black">Fashion</li>
                            <li className="text-sm text-gray-600 hover:text-black">Lifestyle</li>
                            <li className="text-sm text-gray-600 hover:text-black">Denim</li>
                            <li className="text-sm text-gray-600 hover:text-black">Streetstyle</li>
                            <li className="text-sm text-gray-600 hover:text-black">Crafts</li>
                        </ul>
                    </div>
                </div>
            </div>

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
                                    src={product.image}
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
                                        href="product-detail.html"
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