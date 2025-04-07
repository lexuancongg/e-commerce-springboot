'use client'
import {NextPage} from "next";
import {useEffect, useMemo, useState} from "react";
import {CategoryVm as CategoryModel} from "@/models/category/CategoryVm";
import {Container} from "react-bootstrap";
import categoryService from "@/services/category/categoryService";
import CategoryCard from "@/components/category/categoryCard";

const Category: NextPage = () => {
    const [categories, setCategories] = useState<CategoryModel[]>([]);
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


    // event funciton
    const goToPage = (pageNumber: number) => {
        setCurentPage(pageNumber);
    }

    const handleClick = (slug:string)=>{

    }
    // feach Api
    useEffect(() => {
        categoryService.getCategories()
            .then((responseCategoriesVms)=>{
                setCategories(responseCategoriesVms);
            })

    }, []);


    return (
        <Container className="category-container">
            <div className="title">Categories</div>
            <div className="list-categories">
                {currentPage > 1 && (
                    <div className="btn-change-page prev" onClick={() => goToPage(currentPage - 1)}>
                        <i className="bi bi-chevron-left"></i>
                    </div>
                )
                }
                <ul>
                    {chunkedItems.map((chunk, index) => (
                        <li key={index}>
                            {chunk.map((category) => (
                                <CategoryCard category={category} handleClick={handleClick}></CategoryCard>
                            ))}
                        </li>
                    ))}
                </ul>
                {currentPage < totalPages && (
                    <div className="btn-change-page next" onClick={() => goToPage(currentPage + 1)}>
                        <i className="bi bi-chevron-right"></i>
                    </div>
                )}

            </div>


        </Container>
    )
}
export default Category;