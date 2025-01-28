'use client'
import React, {ChangeEvent, useCallback, useEffect, useState} from "react";
import {CartItemDetailVm} from "@/models/cart/cartItemDetailVm";
import CartItem from "@/components/cart/cartItem";
import Link from "next/link";
import {Button} from "react-bootstrap";
import {formatPrice} from "@/utils/formatPrice";
import {useCartContext} from "@/context/cartContext";
import {useUserInfoContext} from "@/context/userInfoContext";
import ConfirmationDialog from "@/components/dialog/confirmDialog";
import {CartItemPutVm} from "@/models/cart/cartItemPutVm";
import cartService from "@/services/cartService";
import {PromotionVerifyResult} from "@/models/promotion/promotion";


const Cart = () => {

    const [cartItems, setCartItems] = useState<CartItemDetailVm[]>([]);

    const [selectedProductIds, setSelectedProductIds] = useState<Set<number>>(new Set());

    const [loadingItems, setLoadingItems] = useState<Set<number>>(new Set());


    const [totalPrice, setTotalPrice] = useState(0);

    const [isShowModelConfirmDelete, setIsShowModelConfirmDelete] = useState(false);

    const [productIdToRemove, setProductIdToRemove] = useState<number>(0);

    const {email} = useUserInfoContext();

    const {fetchNumberCartItems} = useCartContext();

    const [couponCode, setCouponCode] = useState<string>('');

    const [promotionApply, setPromotionApply] = useState<PromotionVerifyResult>({
        couponCode:'bu',
        discountType:'bu',
        isValid:true,
        productId:1,
        discountValue:5
    });

    const [discountMoney, setDiscountMoney] = useState<number>(0);
    const [subTotalPrice, setSubTotalPrice] = useState<number>(0);


    useEffect(() => {
        // fetach api cartItems
        loadCartItems();
    }, [])


    const loadCartItems = useCallback(async () => {
        try {
            const cartItemsPayload = await cartService.getCartItemsFullPayload();
            setCartItems(cartItemsPayload);
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

    const handleSelectCartItemChange = (productId: number) => {
        setSelectedProductIds((prevSelectedProductIds) => {
            // new laij vì set là kiểu tham chiếu
            const newSelectedProductIds = new Set(prevSelectedProductIds);
            if (prevSelectedProductIds.has(productId)) {
                newSelectedProductIds.delete(productId)
            } else {
                newSelectedProductIds.add(productId);
            }
            return newSelectedProductIds;
        })

    }
    const handleUpdateCartItemQuantity = async (productId: number, quantity: number) => {
        const cartItemPutVm: CartItemPutVm = {
            quantity: quantity
        }
        try {
            // call api update tại đây
            await cartService.updateCartItemAboutQuantity(productId, cartItemPutVm);

        } catch (error) {
            throw new Error();
        }

    }
    const handleIncreaseQuantity = async (productId: number) => {
        const cartItem = cartItems.find((item, index) => item.productId == productId);
        if (!cartItem) {
            return;
        }
        const newQuantity = cartItem.quantity + 1;
        await handleUpdateCartItemQuantity(productId, newQuantity);

    }

    const handleDecreaseQuantity = async (productId: number) => {
        const cartItem = cartItems.find((item, index, obj) => {
            return item.productId == productId;
        })
        if (!cartItem) return;
        const newQuantity = cartItem.quantity - 1;
        if (newQuantity < 1) {
            // show confirm log delete product
        } else {

        }

    }

    const handleCartItemQuantityOnBlur = async (
        productId: number, event: React.FocusEvent<HTMLInputElement>
    ) => {
        const newQuantity = parseInt(event.target.value);
        const cartItem = cartItems.find((item, index) => item.productId == productId);
        if (!cartItem || newQuantity === cartItem.quantity) {
            return;
        }
        await handleUpdateCartItemQuantity(productId, newQuantity);
    }

    const handleCartItemQuantityKeyDown = () => {

    }

    const handleOpenDeleteConfirmationModal = () => {

    }


    const removeCouponCode = () => {
        setPromotionApply(undefined);
    };

    const applyCopounCode = () => {

    }

    const handleCheckout = () => {

    }

    const handleDeleteCartItem = (a: any) => {

    }


    return (
        <section className="shop-cart spad">
            <div className="container">
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
                                                promotionApply={promotionApply}
                                                handleSelectCartItemChange={handleSelectCartItemChange}
                                                handleDecreaseQuantity={handleDecreaseQuantity}
                                                handleIncreaseQuantity={handleIncreaseQuantity}
                                                handleCartItemQuantityOnBlur={handleCartItemQuantityOnBlur}
                                                handleCartItemQuantityKeyDown={handleCartItemQuantityKeyDown}
                                                handleOpenDeleteConfirmationModal={handleOpenDeleteConfirmationModal}
                                            />
                                        );
                                    })}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                </div>
                {promotionApply && (
                    <div className="mt-5 mb-5">
                        <div className="row">
                            <div className="col">
                                {promotionApply.couponCode ? (
                                    <span className="coupon-code-apply" aria-hidden="true"
                                          onClick={removeCouponCode}>
                    <i className="bi bi-receipt"></i>
                                        {promotionApply?.discountType === 'PERCENTAGE' ? (
                                            <> You got a {promotionApply.discountValue}% discount on one product!</>
                                        ) : (
                                            <> You got a {promotionApply.discountValue}$ discount on one product!</>
                                        )}
                  </span>
                                ) : (
                                    <span
                                        className="invalid-coupon-code"
                                        style={{color: 'red', fontWeight: 'bold'}}
                                        aria-hidden="true"
                                        onClick={removeCouponCode}
                                    >
                    <i className="bi bi-receipt"></i> Coupon code not valid!
                  </span>
                                )}
                            </div>
                        </div>
                    </div>
                )}
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
                <div className="row">
                    <div className="col-lg-6">
                        <div className="discount__content">
                            <h6>Discount codes</h6>
                            <form action="#">
                                <input
                                    type="text"
                                    placeholder="Enter your coupon code"
                                    onChange={(e) => setCouponCode(e.target.value)}
                                />
                                <button
                                    className="site-btn primary-btn btn btn-primary"
                                    disabled={selectedProductIds.size === 0}
                                    onClick={applyCopounCode}
                                >
                                    Apply
                                </button>
                            </form>
                        </div>
                    </div>
                    <div className="section">
                        <div>
                            LỰA CHỌN CỦA BẠN
                        </div>
                        <div>
                            Tổng <span>{formatPrice(subTotalPrice)}</span>
                        </div>
                        <div>
                            Shop vocher <span>{formatPrice(discountMoney)}</span>
                        </div>
                        <div>
                            <div>
                                <input id="select-all" type="checkbox"/>
                                <label htmlFor="select-all">Chọn Tất Cả</label>
                            </div>
                            <div>Xóa</div>
                            <span>Tổng thanh toán Sản phẩm <span>{formatPrice(totalPrice)}</span>:₫

                        </span>
                            <button className="primary-btn"
                                    onClick={handleCheckout}
                                    disabled={selectedProductIds.size === 0}
                            >Mua Hàng
                            </button>

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