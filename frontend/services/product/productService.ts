import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import apiClient from "@/utils/api/apiClient";
import {productFeaturePagingVm_demo, productsFeaturedMakeSlide_demo} from "@/demo_data/product/product_demo_data";
import {ProductPreviewPagingVm} from "@/models/product/productPreviewPagingVm";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";
import {product_variant_demo_data} from "@/demo_data/product/variant/product_variant_demo_data";
import {ProductDetailVm} from "@/models/product/productDetailVm";
import {Product_detail_demo_data} from "@/demo_data/product/product_detail_demo_data";
import {ProductOptionValueVm} from "@/models/product/options/ProductOptionValueVm";
import {SpecificProductVariantVm} from "@/models/product/specific_variant/SpecificProductVariantGetVm";

class ProductService {
    private baseUrl:string ;

    constructor() {
         this.baseUrl = "/api/product/customer/products"
    }
    public async getProductsByIds(ids : number[]):Promise<ProductPreviewVm[]>{
        const response = await apiClient.get(`${this.baseUrl}/customer/products?productIds=${ids}`)
        if(response.ok){
            return  await response.json();
        }
        return  productsFeaturedMakeSlide_demo;

        throw new Error();
    }





    public async  getFeaturedProductsPaging(pageIndex: number):Promise<ProductPreviewPagingVm>{
        const response = await apiClient.get(`${this.baseUrl}/featured?pageIndex=${pageIndex}`);
        if(response.ok) {
            return await  response.json();
        }
        throw response;
    }


    public async getProductByMultiParams(predicates:string):Promise<ProductPreviewPagingVm>{
        const response  = await apiClient.get(`${this.baseUrl}/customer/products?${predicates}`);
        if(response.ok) return await response.json();
        return productFeaturePagingVm_demo;
        throw response;


    }


    public async getProductVariationsByParentId(parentProductId: number) : Promise<ProductVariantVm[]>{
        return product_variant_demo_data;
        const response = await  apiClient.get(`${this.baseUrl}/product-variations/${parentProductId}`);
        if(response.ok) {
            return  await response.json();
        }

        throw  response;


    }
    public  async  getDetailProductBySlug(slug: string):Promise<ProductDetailVm>{
        return Product_detail_demo_data;
        const response = await apiClient.get(`${this.baseUrl}/${slug}`);
        if(response.ok){
            return await response.json();
        }

        throw  response;

    }

    public async getSpecificProductVariantsByProductId(parentProductId: number):Promise<SpecificProductVariantVm[]>{
        // const response = await apiClient.get(`${this.baseUrl}/specific-product-variants/${parentProductId}`)
        // if(response.ok) return await response.json();
        return [
            {
                id: 10001,
                productOptionId: 101,
                productId: 1001,
                productOptionName: "Màu sắc",
                productOptionValue: "Trắng",
            },
            {
                id: 10002,
                productOptionId: 102,
                productId: 1001,
                productOptionName: "Kích thước",
                productOptionValue: "M",
            },
            {
                id: 10003, // variant: áo trắng size L
                productOptionId: 101,
                productId: 1002,
                productOptionName: "Màu sắc",
                productOptionValue: "Trắng",
            },
            {
                id: 10004,
                productOptionId: 102,
                productId: 1002,
                productOptionName: "Kích thước",
                productOptionValue: "L",
            },

            // Áo thun đen
            {
                id: 10005, // variant: áo đen size M
                productOptionId: 101,
                productId: 1003,
                productOptionName: "Màu sắc",
                productOptionValue: "Đen",
            },
            {
                id: 10006,
                productOptionId: 102,
                productId: 1003,
                productOptionName: "Kích thước",
                productOptionValue: "M",
            },

        ];
        // throw response;
    }
    public async  getProductBestSeller():Promise<ProductPreviewVm[]>{
        return  [];
    }

}
export default new ProductService();
