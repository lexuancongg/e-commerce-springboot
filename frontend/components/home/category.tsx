'use client'
import { NextPage } from "next";
import { useEffect, useMemo, useState } from "react";
import { CategoryVm as CategoryModel } from "@/models/category/CategoryVm";
import { Container } from "react-bootstrap";
import categoryService from "@/services/category/categoryService";
import CategoryCard from "@/components/category/categoryCard";
import { useRouter, useParams, usePathname, useSearchParams } from "next/navigation";
import { category_demo_data } from "@/demo_data/category/category_demo_data";
import LoadImageSafe from "../common/loadImageSafe";

const Category: NextPage = () => {
    const router = useRouter();
    const [categories, setCategories] = useState<CategoryModel[]>(category_demo_data);
    const [currentPage, setCurentPage] = useState<number>(1);
    const defaultItemsPerPage: number = 20;

    // tính tổng page chỉ tính lại khi có su thay đổi
    const totalPages = useMemo(() => {
        return Math.ceil(categories.length / defaultItemsPerPage);
    }, [categories.length])


    // sau này phân trang lai bằng việc callApi kèm pageNo
    const startIndex = useMemo(() => (currentPage - 1) * defaultItemsPerPage, [currentPage]);
    const endIndex = useMemo(() => startIndex + defaultItemsPerPage, [startIndex]);
    const currentItems: CategoryModel[] = categories.slice(startIndex, endIndex);
    // chia ra để hiển thị layout
    const chunkedItems: CategoryModel[][] = [];
    for (let i = 0; i < currentItems.length; i += 2) {
        chunkedItems.push(currentItems.slice(i, i + 2))
    }

    const [current, setCurrent] = useState(0);

    const next = () => {
        if (current < categories.length - 3) setCurrent(current + 1); // show 3 item
    };
    const prev = () => {
        if (current > 0) setCurrent(current - 1);
    };


    // event funciton
    const goToPage = (pageNumber: number) => {
        setCurentPage(pageNumber);
    }

    const handleClick = (slug: string) => {
        router.push(`/products?categorySlug=${slug}`)

    }
    // feach Api
    useEffect(() => {
        categoryService.getCategories()
            .then((responseCategoriesVms) => {
                setCategories(responseCategoriesVms);
            })

    }, []);


    return (
        <Container className="mt-5 relative overflow-hidden  ">
            <div
                className="flex transition-transform duration-500 ease-out"
                style={{
                    transform: `translateX(calc(-${current * (100 / 3)}% - ${current * (1/3 )}rem))`, 
                    gap: "1rem", 
                }}
            >
                {categories.map((category) => (
                    <div
                        key={category.id}
                        className="flex-none relative overflow-hidden cursor-pointer group rounded-xl border border-gray-300 "
                        style={{
                            width: "calc((100% - 2rem) / 3)", 
                            aspectRatio: "4/3", 
                        }}
                    >
                        <LoadImageSafe
                            src={category.categoryImage?.url!}
                            alt={category.name}
                            className="w-full h-full object-cover"
                        />

                        <div className="absolute inset-0 bg-gradient-to-t from-black/30 to-transparent pointer-events-none"></div>

                        <a
                            href={`/category/${category.slug}`}
                            className="absolute inset-0 flex flex-col justify-between p-6"
                        >
                            <div className="flex flex-col space-y-1">
                                <span className="text-black text-2xl font-bold transition-all group-hover:-translate-y-1 group-hover:text-yellow-400">
                                    {category.name}
                                </span>
                                <span className="text-black-100 text-xs transition-all group-hover:-translate-y-1 group-hover:text-yellow-300">
                                    Spring 2018
                                </span>
                            </div>
                            <div className="text-white font-semibold transition-all group-hover:-translate-y-1 group-hover:text-white">
                                Shop Now
                            </div>
                        </a>
                    </div>
                ))}
            </div>

            {/* Prev / Next Buttons */}
            <button
                onClick={prev}
                className="absolute top-1/2 left-0 -translate-y-1/2 bg-black/50 text-white px-3 py-2 rounded-full"
            >
                ‹
            </button>
            <button
                onClick={next}
                className="absolute top-1/2 right-0 -translate-y-1/2 bg-black/50 text-white px-3 py-2 rounded-full"
            >
                ›
            </button>
        </Container>

    )
}
export default Category;
