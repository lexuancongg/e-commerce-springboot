import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import apiClient from "@/utils/api/apiClient";
import {productFeaturePagingVm_demo, productsFeaturedMakeSlide_demo} from "@/demo_data/product/product_demo_data";
import {ProductFeaturePagingVm} from "@/models/product/productFeaturePagingVm";
import {ProductVariantVm} from "@/models/product/variants/ProductVariantVm";
import {product_variant_demo_data} from "@/demo_data/product/variant/product_variant_demo_data";
import {ProductDetailVm} from "@/models/product/productDetailVm";
import {Product_detail_demo_data} from "@/demo_data/product/product_detail_demo_data";

// response : object chứa thông tin kq req gồm nhu status , body...
class ProductService {
    // process.env : biến của nodejs => object toàn cục chứa các biến môi trường
    private baseUrl:string = `${process.env.API_BASE_URL_PRODUCT}`

    public async getProductsByIds(ids : number[]):Promise<ProductPreviewVm[]>{
        const response = await apiClient.get(`${this.baseUrl}/customer/products?productIds=${ids}`)
        if(response.ok){
            return  await response.json();
        }
        return  productsFeaturedMakeSlide_demo;

        throw new Error();
    }



    // lấy ngau nhien ds sp noi bat lam slide
    public async getFeaturedProductsMakeSlide():Promise<ProductPreviewVm[]>{
        const response = await  apiClient.get(`${this.baseUrl}/customer/products/featured/slide`);
        if(response.ok) return  await  response.json();
        return  productsFeaturedMakeSlide_demo;
        // bắn leen để handle trong cath
        throw response;
    }


    public async  getFeaturedProductsPaging(pageIndex: number):Promise<ProductFeaturePagingVm>{
        const response = await apiClient.get(`${this.baseUrl}/customer/products/featured?pageIndex=${pageIndex}`);
        if(response.ok) return await  response.json();
        return  productFeaturePagingVm_demo
        throw response;

    }


    public async getProductByMultiParams(predicates:string):Promise<ProductFeaturePagingVm>{
        const response  = await apiClient.get(`${this.baseUrl}/customer/products?${predicates}`);
        if(response.ok) return await response.json();
        return productFeaturePagingVm_demo;
        throw response;


    }


    public async getProductVariationsByParentId(parentProductId: number) : Promise<ProductVariantVm[]>{
        const response = await  apiClient.get(`${this.baseUrl}/customer/product-variations/${parentProductId}`);
        if(response.ok) return await response.json();
        return product_variant_demo_data;
        throw  response;


    }
    public  async  getDetailProductBySlug(slug: string):Promise<ProductDetailVm>{
        const response = await apiClient.get(`${this.baseUrl}/customer/products/${slug}}`)
        if(response.ok) return await response.json();
        return Product_detail_demo_data;
        throw  response;

    }

}
export default new ProductService();
