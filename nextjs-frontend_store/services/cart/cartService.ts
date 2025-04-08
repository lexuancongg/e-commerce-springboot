import {CartItemDetailVm} from "@/models/cart/CartItemDetailVm";
import {CartItemVm} from "@/models/cart/CartItemVm";
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


    public async getCartItemDetails():Promise<CartItemDetailVm[]>{

        const cartItems:CartItemVm[] = await this.getCartItems();
        const cartItemProductIds:number[] = cartItems.map(cartItem => cartItem.productId);
        const products:ProductPreviewVm[] = await productService.getProductsByIds(cartItemProductIds);

        return this.mergeCartItemsWithProductInfo(cartItems,products);
    }

    public mergeCartItemsWithProductInfo(cartItems : CartItemVm[] , productCartItemPreview:ProductPreviewVm[]):CartItemDetailVm[]{
        const cartItemsDetailVm:CartItemDetailVm[] = [];
        // cartItems.forEach(cartItem =>{
        //     const product = cartItemProducts.find((item)=>item.id == cartItem.productId);
        //     if(product){
        //         const cartItemDetailVm : CartItemDetailVm ={
        //             productId:product.id,
        //             quantity:cartItem.quantity,
        //             imageUrl:product.imageUrl,
        //             price:product.price,
        //             productName:product.name,
        //             slug:product.slug
        //         }
        //         cartItemsDetailVm.push(cartItemDetailVm);
        //     }
        // })
        // return cartItemsDetailVm;

        const productMap :Map<number,ProductPreviewVm> =
            new Map(productCartItemPreview.map((product,index)=>[product.id,product]));
        for(const cartItem of cartItems){
            const product = productMap.get(cartItem.productId);
            if(!product){
                continue;
            }
            cartItemsDetailVm.push(
                {
                    ...cartItem,
                    productName: product.name,
                    slug: product.slug,
                    imageUrl: product.avatarUrl,
                    price: product.price,
                }
            );


        }
        return cartItemsDetailVm;
    }

    public async getCartItems():Promise<CartItemVm[]>{

        const response = await apiClient.get( `${this.baseUrl}/customer/cart/items`);

        if(response.ok){
            return await response.json();
        }
        return cartItems_demo_data;
        throw new Error();
    }

    public async updateCartItemAboutQuantity(productId:number , cartItemPutVm:CartItemPutVm){
        const response = await apiClient.put(this.baseUrl,JSON.stringify(cartItemPutVm));
        if(!response.ok){
            throw new Error();
        }
        return await response.json();

    }


}

export default  new CartService();