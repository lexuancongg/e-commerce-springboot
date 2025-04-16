'use client'
import  yup from 'yup';
import {useParams, useSearchParams} from "next/navigation";
import {useState} from "react";
import {CheckoutVm} from "@/models/order/checkout/CheckoutVm";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import {AddressFieldForm} from "@/models/address/AddressFieldForm";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import {responsivePropType} from "react-bootstrap/createUtilityClasses";
import {Container} from "react-bootstrap";
import Loading from "@/app/loading";
import AddressForm from "@/components/address/addressForm";
import Input from "@/components/item/input";
import CheckOutAddress from "@/components/order/CheckOutAddress";
import ModalAddressList from "@/components/order/ModalAddressList";
const phoneRegExp =
    /^((\+[1-9]{1,4}[ -]*)|(\([0-9]{2,3}\)[ -]*)|[0-9]{2,4}[ -]*)?[0-9]{3,4}?[ -]*[0-9]{3,4}?$/;
const addressShippingSchema = yup.object({
    contactName: yup.string().required('contactName is Require'),
    phoneNumber:yup.string().matches(phoneRegExp ,'phoneNumber is not valid').required(''),
    specificAddress: yup.string().required("specificAddress is require"),
    districtId: yup.number().required('District is required'),
    countryId: yup.number().required('Country is required'),
    provinceId :yup.number().required('Country is required')

})
const Checkout = ()=>{
    const params = useParams();
    const id = params.id;

    const [checkout,setCheckout] = useState<CheckoutVm>()
    const {
        register:registerShippingAddress,
        handleSubmit :handleSubmitShippingAddress,
        formState:{errors:errorsShippingAddress},
        setValue:setValueShippingAddress
    } = useForm<AddressDetailVm>({resolver:yupResolver(addressShippingSchema)})

    const [shippingAddress, setShippingAddress] = useState<AddressDetailVm>();
    const [isAddShippingAddress,setIsAddShippingAddress] = useState<boolean>(false);
    const  [isShowModelShippingAddress,setIsShowModelShippingAddress] = useState<boolean>(false);
    const [disableProcessPayment, setDisableProcessPayment] = useState(false);
    const [isShowSpinner , setIsShowSpinner] = useState<boolean>(false);




    return (
        <Container>
            <section className="checkout spad">
                {isShowSpinner ? <Loading></Loading> :''}
                <div className="container">
                    <div className="checkout__form">
                        <form onSubmit={(event) => }>
                            <div className="row">
                                <div className="col-lg-8 col-md-6">
                                    <h4>Shipping Address</h4>
                                    <div className="row mb-4">
                                        <div className="checkout__input">
                                            <button
                                                type="button"
                                                className="btn btn-outline-primary fw-bold btn-sm me-2"
                                                onClick={() => {
                                                    setIsAddShippingAddress(false);
                                                    setIsShowModelShippingAddress(true);
                                                }}
                                            >
                                                Change address <i className="bi bi-plus-circle-fill"></i>
                                            </button>
                                            <button
                                                type="button"
                                                className={`btn btn-outline-primary fw-bold btn-sm ${
                                                    isAddShippingAddress ? `active` : ``
                                                }`}
                                                onClick={() => setIsAddShippingAddress(true)}
                                            >
                                                Add new address <i className="bi bi-plus-circle-fill"></i>
                                            </button>
                                        </div>
                                        <div className="checkout-address-card">
                                            <CheckOutAddress address={shippingAddress} isDisplay={!isAddShippingAddress}/>
                                            <AddressForm
                                                handleSubmit={}
                                                isDisplay={isAddShippingAddress}
                                                register={registerShippingAddress}
                                                setValue={setValueShippingAddress}
                                                errors={errorsShippingAddress}
                                                address={undefined}
                                                buttonText="Use this address"
                                            />
                                        </div>
                                    </div>


                                    <h4>Additional Information</h4>
                                    <div className="row mb-4">
                                        <div className="checkout__input">
                                            <Input
                                                type="text"
                                                labelText="Order Notes"
                                                fieldName="note"
                                                register={}
                                                placeholder="Notes about your order, e.g. special notes for delivery."
                                                error={errors.note?.message}
                                            />
                                        </div>
                                        <div className="checkout__input">
                                            <Input
                                                type="text"
                                                labelText="Email"
                                                fieldName="email"
                                                register={+}
                                                defaultValue={checkout?.email}
                                                error={errors.note?.message}
                                                disabled={true}
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-lg-4 col-md-6">

                                </div>
                            </div>
                        </form>
                        <ModalAddressList
                            isShow={isShowModelShippingAddress}
                            handleCloseModel={()=>{}}
                            handleSelectAddress={()=>{}}
                            // defaultUserAddress={shippingAddress}
                            // selectedAddressId={shippingAddress?.id}
                        />

                    </div>
                </div>
            </section>
        </Container>
    )


}
export default Checkout;