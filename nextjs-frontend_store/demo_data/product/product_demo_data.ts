import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {ProductPreviewPagingVm} from "@/models/product/productPreviewPagingVm";

export const productsFeaturedMakeSlide_demo : ProductPreviewVm[] = [
    {
        id:1,
        avatarUrl: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        name:'name',
        price:50,
        slug:'slug'
    },
    {
        id:2,
        avatarUrl:'https://obs.line-scdn.net/0hYVn9GL_6BnlTQBPC0sJ5LmoWCghgJBN_PThKTyEUDUF_dl18aHUbA3MQW0FieEJ6aTpIHnFHX0B8eEJ8PC4',
        name:'name',
        price:50,
        slug:'slug'
    },
    {
        id:3,
        avatarUrl: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        name:'name',
        price:50,
        slug:'slug'
    },
    {
        id:4,
        avatarUrl: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        name:'name',
        price:50,
        slug:'slug'
    },
    {
        id:5,
        avatarUrl: 'https://luvinus.com/wp-content/uploads/2020/01/cach-phoi-ao-len.jpg',
        name:'name',
        price:50,
        slug:'slug'
    },
]


export  const productFeaturePagingVm_demo : ProductPreviewPagingVm = {
    productPreviewsPayload: [
        { id: 1, name: "Product 1", slug: "product-1", price: 100000, avatarUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
        { id: 2, name: "Product 2", slug: "product-2", price: 150, avatarUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
        { id: 3, name: "Product 3", slug: "product-3", price: 200, avatarUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
        { id: 4, name: "Product 4", slug: "product-4", price: 250, avatarUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
        { id: 5, name: "Product 5", slug: "product-5", price: 300, avatarUrl: "https://th.bing.com/th/id/OIP.j42T-H3P0LqlfQ1TVU8zggHaF7?rs=1&pid=ImgDetMain" },
    ],
    pageIndex:1,
    pageSize:10,
    totalPages:15,
    isLast:false,
    totalElements:50
}
