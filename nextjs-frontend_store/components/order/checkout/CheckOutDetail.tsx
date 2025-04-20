import {FC, useState} from "react";
import {OrderItemVm} from "@/models/order/OrderItemVm";
import userAddressService from "@/services/customer/userAddressService";
import {CheckoutItemVm} from "@/models/order/checkout/CheckoutItemVm";
import {formatPrice} from "@/utils/formatPrice";
type Props = {
    checkoutItems :CheckoutItemVm[],
    isPaymentEnabled : boolean
}
const CheckOutDetail :FC<Props> = ({checkoutItems,isPaymentEnabled})=>{
    const [totalPrice, setTotalPrice] = useState(0);

    return (
        <>
            <div className="checkout__order">
                <h4>Your Order</h4>
                <div className="checkout__order__products row">
                    <div className="col-lg-6">Products</div>
                    <div className="col-lg-4">Quantity</div>
                    <div className="col-lg-2">Price</div>
                </div>

                {checkoutItems?.map((item) => (
                    <div key={item.productId} className="row">
                        <div className="col-lg-6">{item.productName} </div>
                        <div className="col-lg-3 d-flex justify-content-center">{item.quantity}</div>
                        <div className="col-lg-3"> {formatPrice(item.productPrice)}</div>
                    </div>
                ))}

                <div className="checkout__order__total">
                    Total <span>{formatPrice(totalPrice)}</span>
                </div>
                <div className="checkout__order__payment__providers">
                    <h4>Payment Method</h4>
                    {paymentProviders.map((provider) => (
                        <div
                            className={`payment__provider__item ${
                                selectedPayment === provider.id ? 'payment__provider__item__active' : ''
                            }`}
                            key={provider.id}
                        >
                            <label>
                                <input
                                    type="radio"
                                    name="paymentMethod"
                                    value={provider.id}
                                    checked={selectedPayment === provider.id}
                                    onChange={() => paymentProviderChange(provider.id)}
                                />
                                {provider.name}
                            </label>
                        </div>
                    ))}
                </div>
                <div className="checkout__input__checkbox">
                    <label htmlFor="acc-or">
                        Agree to Terms and Conditions
                        <input type="checkbox" id="acc-or"  />
                        <span className="checkmark"></span>
                    </label>
                </div>
                <p className="mb-2">
                    “I agree to the terms and conditions as set out by the user agreement.{' '}
                    <a className="text-primary" href="./conditions">
                        Learn more
                    </a>
                    ”
                </p>

                <button
                    type="submit"
                    className="site-btn"
                    disabled={isPaymentEnabled ? true : isPaymentEnabled}
                    style={
                        isPaymentEnabled || disableCheckout
                            ? { cursor: 'not-allowed', backgroundColor: 'gray' }
                            : { cursor: 'pointer' }
                    }
                >
                    Process to Payment
                </button>
            </div>
        </>
    );

}
export default CheckOutDetail;