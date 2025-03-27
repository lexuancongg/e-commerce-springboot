import {FC} from "react";
import {CartItemDetailVm} from "@/models/cart/CartItemDetailVm";
import LoadImageSafe from "@/components/common/loadImageSafe";
import Link from "next/link";
import {formatPrice} from "@/utils/formatPrice";
// import {PromotionVerifyResult} from "@/models/promotion/promotion";

interface CartItemProps {
    item:CartItemDetailVm;
    isLoading: boolean;
    isSelected: boolean;
    handleSelectCartItemChange: (productId: number) => void;
    handleDecreaseQuantity: (productId: number) => void;
    handleIncreaseQuantity: (productId: number) => void;
    handleCartItemQuantityOnBlur: (
        productId: number,
        event: React.FocusEvent<HTMLInputElement>
    ) => void;
    handleCartItemQuantityKeyDown: (
        productId: number,
        event: React.KeyboardEvent<HTMLInputElement>
    ) => void;
    handleShowModelConfirmDelete: (productId: number) => void;
}


const CartItem: FC<CartItemProps> = ({
                                         item,
                                         isLoading,
                                         isSelected,
                                         handleSelectCartItemChange,
                                         handleDecreaseQuantity,
                                         handleIncreaseQuantity,
                                         handleCartItemQuantityOnBlur,
                                         handleCartItemQuantityKeyDown,
                                         handleShowModelConfirmDelete,
                                     }) => {
    return (
        <tr key={item.quantity.toString() + item.productId.toString()}>
            <td>
                <label className="item-checkbox-label" htmlFor="select-item-checkbox">
                    {''}
                    <input
                        className="form-check-input item-checkbox"
                        type="checkbox"
                        checked={isSelected}
                        // sử lý sự kiện cho chọn cartItem
                        onChange={() => handleSelectCartItemChange(item.productId)}
                    />
                </label>
            </td>
            <td className="cart__product__item d-flex align-items-center">
                <div className="h-100">
                    <Link
                        href={{
                            pathname: '/redirect',
                            query: { productId: item.productId },
                        }}
                    >
                        <LoadImageSafe
                            src={item.imageUrl}
                            alt={item.productName}
                            style={{ width: '120px', height: '120px', cursor: 'pointer' }}
                        />
                    </Link>
                </div>
                <div className="cart__product__item__title pt-0">
                    <Link
                        href={{
                            pathname: '/redirect',
                            query: { productId: item.productId },
                        }}
                    >
                        <h6 className="product-link">{item.productName}</h6>
                    </Link>
                </div>
            </td>
            <td className="cart__price">

            </td>
            <td className="cart__quantity">
                <div className="pro-qty">
                    <div className={`quantity buttons_added ${isLoading ? 'disabled' : ''}`}>
                        <button
                            id="minus-button"
                            type="button"
                            className="minus"
                            onClick={() => handleDecreaseQuantity(item.productId)}
                            disabled={isLoading}
                        >
                            -
                        </button>

                        <input
                            id="quanity"
                            type="number"
                            step="1"
                            min="1"
                            max=""
                            name="quantity"
                            defaultValue={item.quantity}
                            onBlur={(e) => handleCartItemQuantityOnBlur(item.productId, e)}
                            onKeyDown={(e) => handleCartItemQuantityKeyDown(item.productId, e)}
                            title="Qty"
                            className="input-text qty text"
                            disabled={isLoading}
                        />
                        <button
                            id="plus-button"
                            type="button"
                            className="plus"
                            onClick={() => handleIncreaseQuantity(item.productId)}
                            disabled={isLoading}
                        >
                            +
                        </button>
                    </div>
                </div>
            </td>
            <td className="cart__total">{item.price}</td>
            <td className="cart__close">
                {' '}
                <button
                    className="remove_product"
                    onClick={() => handleShowModelConfirmDelete(item.productId)}
                >
                    <i className="bi bi-x-lg fs-5"></i>
                </button>{' '}
            </td>
        </tr>
    );
};

export default CartItem;
