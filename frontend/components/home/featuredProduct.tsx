'use client'
import { FC, useState } from "react";
import { Container } from "react-bootstrap";

interface Product {
    id: string;
    name: string;
    price: number;
    image: string;
    category: string; // ví dụ 'women', 'men', 'bag', ...
}

interface Props {
    products: Product[];
}

const ProductOverview = () => {
    const products = [
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



    const categories = ["All Products", "Women", "Men", "Bag", "Shoes", "Watches"];


    return (
        <Container className=" mt-16  ">
            <div className="mb-4">
                <h3 className="text-4xl font-bold text-gray-900">Product Overview</h3>
            </div>

            {/* Tabs */}
            <div className="flex items-center justify-between border-b pb-4 mb-8">
                <div className="flex space-x-8">
                    {categories.map((cat) => (
                        <button
                            key={cat}
                            onClick={() => setActiveTab(cat)}
                            className={`pb-2 border-b-2 transition-all duration-200 ${activeTab == cat
                                ? "text-black font-medium border-red-500"
                                : "text-gray-500 border-transparent hover:text-black hover:border-gray-300"
                                }`}
                        >
                            {cat}
                        </button>
                    ))}
                </div>


                {/* Actions */}
                <div className="flex space-x-3">
                    <button className="flex items-center border px-4 py-2 rounded hover:bg-gray-100 text-sm">
                        <span className="material-icons mr-1 text-base">filter_list</span>
                        Filter
                    </button>
                    <button className="flex items-center border px-4 py-2 rounded hover:bg-gray-100 text-sm">
                        <span className="material-icons mr-1 text-base">search</span>
                        Search
                    </button>
                </div>
            </div>

            {/* Products grid */}
            <div className="row isotope-grid gap-y-8">
                {products.map((product) => (
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

            <div className="flex justify-center mt-10">
                <a
                    href="#"
                    className="px-6 py-2 text-sm font-medium text-black bg-gray-300 rounded hover:!text-white hover:!bg-black transition"
                >
                    Load More
                </a>
            </div>


        </Container>


    );
};

export default ProductOverview;



