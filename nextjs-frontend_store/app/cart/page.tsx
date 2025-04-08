'use client'
import React, {ChangeEvent, useCallback, useEffect, useState} from "react";
import {CartItemDetailVm} from "@/models/cart/CartItemDetailVm";
import CartItem from "@/components/cart/cartItem";
import Link from "next/link";
import {Button} from "react-bootstrap";
import {formatPrice} from "@/utils/formatPrice";
import ConfirmationDialog from "@/components/dialog/confirmDialog";
import {CartItemPutVm} from "@/models/cart/CartItemPutVm";
import cartService from "@/services/cart/cartService";
import {useCartContext} from "@/context/cartContext";
import NavigationComponent from "@/components/common/navigationComponent";
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";

const navigationPaths : NavigationPathModel[] = [
    {
        pageName :'Home',
        url:'#'
    },
    {
        pageName:'My-cart',
        url:'#'
    }
];


const Cart = () => {
    // thông tin cartItem
    const [cartItems, setCartItems] = useState<CartItemDetailVm[]>([]);
    // ds productId đã chọn
    const [selectedProductIds, setSelectedProductIds] = useState<Set<number>>(new Set());

    const [loadingItems, setLoadingItems] = useState<Set<number>>(new Set());


    const [isShowModelConfirmDelete, setIsShowModelConfirmDelete] = useState(false);

    const [productIdToRemove, setProductIdToRemove] = useState<number>(0);

    const { fetchNumberCartItems } = useCartContext();


    useEffect(() => {
        loadCart()
            .catch(error=> console.log(error))
    }, [])



    // get data cartItems
    const loadCart = useCallback(async () => {
        try {
            const cartItemDetailsPayload = await cartService.getCartItemDetails();
            setCartItems(cartItemDetailsPayload);
            fetchNumberCartItems();
        } catch (error) {
            throw new Error();
        }
    }, [])


    // click chọn tất cả - done
    const handleSelectAllCartItemsChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.checked) {
            const allProductIds: number[] = cartItems.map(cartItem => cartItem.productId);
            setSelectedProductIds(new Set(allProductIds));
            return;
        }
        setSelectedProductIds(new Set());

    }

    // chọn từng cartItem
    const handleSelectCartItemChange = (productId: number) => {
        setSelectedProductIds((prevSelectedProductIds) => {
            // new lại vì set là kiểu tham chiếu
            const newSelectedProductIds = new Set(prevSelectedProductIds);
            if (prevSelectedProductIds.has(productId)) {
                newSelectedProductIds.delete(productId)
            } else {
                newSelectedProductIds.add(productId);
            }
            return newSelectedProductIds;
        })

    }


    // handle update quantity cartItem
    const handleUpdateCartItemQuantity = async (productId: number, quantity: number) => {
        const cartItemPutVm: CartItemPutVm = {
            quantity: quantity
        }
        try {
            // call api update tại đây
            await cartService.updateCartItemAboutQuantity(productId, cartItemPutVm);
            loadCart();

        } catch (error) {
            throw new Error();
        }
    }
    // tăng số lượng 1
    const handleIncreaseQuantity = async (productId: number) => {
        const cartItem = cartItems.find((item, index) => item.productId == productId);
        if (!cartItem) {
            return;
        }
        const newQuantity = cartItem.quantity + 1;
        await handleUpdateCartItemQuantity(productId, newQuantity);

    }


    // khi giảm số lượng xuống 1
    const handleDecreaseQuantity = async (productId: number) => {
        const cartItem = cartItems.find((item, index, obj) => {
            return item.productId == productId;
        })
        if (!cartItem) return;
        const newQuantity = cartItem.quantity - 1;
        if (newQuantity < 1) {
            // show confirm log delete product
            handleShowModelConfirmDelete(productId);
        } else {
            await handleUpdateCartItemQuantity(productId,newQuantity);

        }

    }

    const handleShowModelConfirmDelete = (productId:number)=>{
        setProductIdToRemove(productId);
        setIsShowModelConfirmDelete(true);
    }



    const handleCartItemQuantityOnBlur = async (
        productId: number, event: React.FocusEvent<HTMLInputElement>
    ) => {
        const newQuantity = parseInt(event.target.value.trim(),10);
        if(isNaN(newQuantity)|| newQuantity  <=0){
            event.preventDefault();
            return;

        }
        const cartItem = cartItems.find((item, index) => item.productId == productId);
        if (!cartItem || newQuantity === cartItem.quantity) {
            return;
        }
        await handleUpdateCartItemQuantity(productId, newQuantity);
    }


    const handleCartItemQuantityKeyDown = (productId:number,event: React.KeyboardEvent<HTMLInputElement>) => {
        const allowedKeys : string[] = ['Backspace', 'ArrowLeft', 'ArrowRight', 'Delete', 'Tab', 'Enter'];
        const regExpNumber = /^\d$/;
        if(!allowedKeys.includes(event.key) && !regExpNumber.test(event.key)){
            event.preventDefault();
            return
        }
        if(event.key == 'Enter'){
            const newQuantity =parseInt(event.currentTarget.value.trim(),10);
            if(isNaN(newQuantity)|| newQuantity  <=0){
                event.preventDefault();
                return;

            }

        }


    }

    const handleShowDeleteConfirmationModal = () => {

    }



    const applyCopounCode = () => {

    }

    const handleCheckout = () => {

    }

    const handleDeleteCartItem = (a: any) => {

    }


    return (
        <section className="shop-cart spad">
            <div className="container">
                <NavigationComponent props={navigationPaths}></NavigationComponent>
                <div className="row">
                    <div className="col-lg-12">
                        <div className="shop__cart__table">
                            {cartItems.length === 0 ? (
                                <h4>There are no items in this cart.</h4>
                            ) : (
                                <table>
                                    <thead>
                                    <tr>
                                        <th>
                                            <label className="item-checkbox-label" htmlFor="select-all-checkbox">
                                                {''}
                                                <input
                                                    id="select-all-checkbox"
                                                    type="checkbox"
                                                    className="form-check-input item-checkbox"
                                                    onChange={handleSelectAllCartItemsChange}
                                                    checked={selectedProductIds.size === cartItems.length}
                                                />
                                            </label>
                                        </th>
                                        <th>Product</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Total</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {cartItems.map((item) => {
                                        return (
                                            <CartItem
                                                key={item.productId}
                                                item={item}
                                                isLoading={loadingItems.has(item.productId)}
                                                isSelected={selectedProductIds.has(item.productId)}
                                                handleSelectCartItemChange={handleSelectCartItemChange}
                                                handleDecreaseQuantity={handleDecreaseQuantity}
                                                handleIncreaseQuantity={handleIncreaseQuantity}
                                                handleCartItemQuantityOnBlur={handleCartItemQuantityOnBlur}
                                                handleCartItemQuantityKeyDown={handleCartItemQuantityKeyDown}
                                                handleShowModelConfirmDelete={handleShowModelConfirmDelete}
                                            />
                                        );
                                    })}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                </div>

                <div className="row">
                    <div className="col-lg-6 col-md-6 col-sm-6">
                        <div>
                            <Link href={'/'}>
                                <Button className="cart__btn2">
                                    <i className="bi bi-house-fill"></i> CONTINUE SHOPPING
                                </Button>
                            </Link>
                        </div>
                    </div>
                </div>

                <ConfirmationDialog
                    isOpen={isShowModelConfirmDelete}
                    okText="Remove"
                    cancelText="Cancel"
                    cancel={() => setIsShowModelConfirmDelete(false)}
                    ok={() => handleDeleteCartItem(productIdToRemove)}
                >
                    <p>Do you want to remove this Product from the cart ?</p>
                </ConfirmationDialog>
            </div>

        </section>
    );
}
export default Cart;