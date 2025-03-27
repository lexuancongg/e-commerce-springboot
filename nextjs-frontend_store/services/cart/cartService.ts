import {CartItemDetailVm} from "@/models/cart/CartItemDetailVm";
import {CartItemVm} from "@/models/cart/CartItemVm";
import apiClient from "@/utils/api/apiClient";
import productService from "@/services/product/productService";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {CartItemPutVm} from "@/models/cart/CartItemPutVm";

class CartService{
    private  baseUrl : string = "api";
    public  async getNumberCartItems():Promise<number>{
        return 1;
    }


    public async getCartItemsFullPayload():Promise<CartItemDetailVm[]>{
        return [
            {
                productId: 101,
                quantity: 2,
                productName: "Product A",
                slug: "product-a",
                imageUrl: "https://th.bing.com/th/id/OIP.VSnbm5qgNu_BDNvyBa0I5AHaHa?rs=1&pid=ImgDetMain",
                price: 199.99,
            },
            {
                productId: 102,
                quantity: 1,
                productName: "Product B",
                slug: "product-b",
                imageUrl: "https://th.bing.com/th/id/OIP.VSnbm5qgNu_BDNvyBa0I5AHaHa?rs=1&pid=ImgDetMain",
                price: 299.99,
            },
            {
                productId: 103,
                quantity: 3,
                productName: "Product C",
                slug: "product-c",
                imageUrl: "https://th.bing.com/th/id/OIP.VSnbm5qgNu_BDNvyBa0I5AHaHa?rs=1&pid=ImgDetMain",
                price: 149.99,
            },

        ];




        const cartItems:CartItemVm[] = await this.getCartItems();
        const cartItemProductIds:number[] = cartItems.map(cartItem => cartItem.productId);
        const products:ProductPreviewVm[] = await productService.getProductsByIds(cartItemProductIds);

        return this.mergeCartItemsToProductDetails(cartItems,products);
    }

    public mergeCartItemsToProductDetails(cartItems : CartItemVm[] , productCartItemPreview:ProductPreviewVm[]):CartItemDetailVm[]{
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
                    imageUrl: product.imageUrl,
                    price: product.price,
                }
            );


        }
        return cartItemsDetailVm;


    }

    public async getCartItems():Promise<CartItemVm[]>{

        return  [
            { productId: 1, quantity: 2 },
            { productId: 2, quantity: 5 },
            { productId: 3, quantity: 1 },
            { productId: 4, quantity: 3 },
            { productId: 5, quantity: 4 },
        ];

        const response = await apiClient.get(this.baseUrl);

        if(response.ok){
            return await response.json();
        }
        console.log("get cartItems error")
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