import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import apiClient from "@/utils/api/apiClient";
import {productsFeaturedMakeSlide} from "@/demo_data/product/product_demo_data";

// response : object chứa thông tin kq req gồm nhu status , body...
class ProductService {
    // process.env : biến của nodejs => object toàn cục chứa các biến môi trường
    private base_url:string = `${process.env.API_BASE_URL_PRODUCT}`

    public async getProductsByIds(ids : number[]):Promise<ProductPreviewVm[]>{
        const response = await apiClient.get(`${this.base_url}/product...`)
        if(response.ok){
            return  await response.json();
        }
        throw new Error();
    }

    // lấy ngau nhien ds sp noi bat lam slide
    public async getProductFeaturedMakeSlide():Promise<ProductPreviewVm[]>{
        const response = await  apiClient.get(`${this.base_url}/customer/products/featured/slide`);
        if(response.ok) return  await  response.json();
        return  productsFeaturedMakeSlide;
        // bắn leen để handle trong cath
        throw response;
    }

}
export default new ProductService();
