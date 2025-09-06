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
        <Container className="mt-5">
            <div className="row">
                {categories.map(category =>
                (
                    <div className="col-md-6 col-xl-4 p-b-30 m-lr-auto">
                        <div className="block1 wrap-pic-w relative overflow-hidden cursor-pointer group">
                            <LoadImageSafe
                                src={category.categoryImage?.url!}
                                alt="IMG-BANNER"
                                className="w-full h-full object-cover"
                            />

                            <div className="absolute inset-0 bg-gradient-to-t from-black/30 to-transparent pointer-events-none"></div>

                            <a
                                href="product.html"
                                className="absolute inset-0 flex flex-col justify-between p-8"
                            >
                                {/* Text */}
                                <div className="flex flex-col space-y-1">
                                    <span className="text-white text-xl font-bold transition-all group-hover:-translate-y-1 group-hover:text-yellow-400">
                                        Women
                                    </span>
                                    <span className="text-white text-sm transition-all group-hover:-translate-y-1 group-hover:text-yellow-300">
                                        Spring 2018
                                    </span>
                                </div>

                                {/* Shop Now */}
                                <div className="text-white font-semibold transition-all group-hover:-translate-y-1 group-hover:text-white">
                                    Shop Now
                                </div>
                            </a>
                        </div>
                    </div>
                )

                )}




            </div>
        </Container>

    )
}
export default Category;
