import {FC, useEffect, useState} from "react";
import {CheckoutItemVm} from "@/models/order/checkout/CheckoutItemVm";
import {formatPrice} from "@/utils/formatPrice";
import {PaymentProvider} from "@/models/payment/PaymentProvider";
import paymentProviderService from "@/services/payment/PaymentProviderService";

type Props = {
    checkoutItems: CheckoutItemVm[],
    isPaymentEnabled: boolean
}
const CheckOutDetail: FC<Props> = ({checkoutItems, isPaymentEnabled}) => {
    const [totalPrice, setTotalPrice] = useState(0);
    // danh sách các payment hệ thống tích hợp
    const [paymentProviders, setPaymentProviders] = useState<PaymentProvider[]>([]);
    // xem checkout có được thông qua chưa ví dụ nếu chưa có địa chỉ nhận hàng thi chặn lại k cho thanh toán
    const [isCheckoutEnabled, setIsCheckoutEnabled] = useState(false);
    const [selectedPaymentType, setSelectedPaymentType] = useState<string>();
    const [selectedPaymentProviderId, setSelectedPaymentProviderId] = useState<number>();
    useEffect(() => {
        // get payment provider ra cho người dùng chọn
        paymentProviderService.getPaymentProviderEnable()
            .then((responsePaymentProviders) => {
                setPaymentProviders(responsePaymentProviders)
            })
    }, []);

    useEffect(() => {
        if (paymentProviders.length && selectedPaymentProviderId) {
            setSelectedPaymentProviderId(paymentProviders[0].id);
        }
    }, [paymentProviders]);


    const handleChangePaymentProvider = (paymentProviderId: number) => {
        setSelectedPaymentProviderId(paymentProviderId)
    }


    const handelChangeAgreePolicy = (event: any) => {
        if (event.target.checked) {
            setIsCheckoutEnabled(true);
            return;
        }
        setIsCheckoutEnabled(false)
    }

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
                                selectedPaymentProviderId === provider.id ? 'payment__provider__item__active' : ''
                            }`}
                            key={provider.id}
                        >
                            <label>
                                <input
                                    type="radio"
                                    name="paymentMethod"
                                    value={provider.id}
                                    checked={selectedPaymentProviderId === provider.id}
                                    onChange={() => handleChangePaymentProvider(provider.id)}
                                />
                                {provider.name}
                            </label>
                        </div>
                    ))}
                </div>
                <div className="checkout__input__checkbox">
                    <label htmlFor="acc-or">
                        Agree to Terms and Conditions
                        <input type="checkbox" id="acc-or" onChange={handelChangeAgreePolicy}/>
                        <span className="checkmark"></span>
                    </label>
                </div>

                <button
                    type="submit"
                    className="site-btn"
                    disabled={isPaymentEnabled ? true : isPaymentEnabled}
                    style={
                        !(isPaymentEnabled || isCheckoutEnabled)
                            ? {cursor: 'not-allowed', backgroundColor: 'gray'}
                            : {cursor: 'pointer'}
                    }
                >
                    Process to Payment
                </button>
            </div>
        </>
    );

}
export default CheckOutDetail;