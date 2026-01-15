import {CartItemDetailVm} from "@/models/cart/CartItemDetailVm";

import apiClient from "@/utils/api/apiClient";
import productService from "@/services/product/productService";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {CartItemPutVm} from "@/models/cart/CartItemPutVm";
import {cartItems_demo_data} from "@/demo_data/cart/cart_demo_data";

class CartService{
    private  baseUrl : string = `${process.env.API_BASE_URL_CART}`;
    public  async getNumberCartItems():Promise<number>{
        return 1;
    }


    public async updateCartItemAboutQuantity(productId:number , cartItemPutVm:CartItemPutVm){
        const response = await apiClient.put(
            "/api/cart/customer/cart-items/"+productId
            ,JSON.stringify(cartItemPutVm)
        );
        if(!response.ok){
           throw response;
        }
        return await response.json();

    }

    public async  getCartItems():Promise<CartItemDetailVm[]>{
        const response = await apiClient.get("/api/cart/customer/cart-items")
        if(response.ok){
            return await  response.json();
        }
        throw response;
    }

    public  deleteCartItem = async (productId: number)=>{
        const response = await apiClient.delete("/api/cart/customer/cart-items/" + productId)
        if(!response.ok){
            throw response;
        }

    }


}

export default  new CartService();