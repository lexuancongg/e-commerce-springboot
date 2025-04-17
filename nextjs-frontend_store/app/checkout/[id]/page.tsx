'use client'
import  * as yup from 'yup';
import {useParams} from "next/navigation";
import {useEffect, useState} from "react";
import {CheckoutVm} from "@/models/order/checkout/CheckoutVm";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import {Container} from "react-bootstrap";
import Loading from "@/app/loading";
import AddressForm from "@/components/address/addressForm";
import Input from "@/components/item/input";
import CheckOutAddress from "@/components/order/checkout/CheckOutAddress";
import ModalAddressList from "@/components/order/ModalAddressList";
import {OrderDetailVm} from "@/models/order/OrderDetailVm";
import userAddressService from "@/services/customer/userAddressService";
import orderService from "@/services/order/orderService";
import {CheckoutItemVm} from "@/models/order/checkout/CheckoutItemVm";
import {log} from "node:util";
import {date} from "yup";
import {AddressPostVm} from "@/models/address/AddressPostVm";
import addressService from "@/services/address/addressService";
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
    const [order,setOrder] = useState<OrderDetailVm>()
    const [disableProcessPayment, setDisableProcessPayment] = useState(false);
    const [isShowSpinner , setIsShowSpinner] = useState<boolean>(false);
    const [shippingAddress, setShippingAddress] = useState<AddressDetailVm>();
    const [isAddShippingAddress,setIsAddShippingAddress] = useState<boolean>(false);
    const  [isShowModalShippingAddress,setIsShowModalShippingAddress] = useState<boolean>(false);
    const [currentAddressId,setCurrentAddressId] = useState<number>()


    const {
        register:registerShippingAddress,
        handleSubmit :handleSubmitShippingAddress,
        formState:{errors:errorsShippingAddress},
        setValue:setValueShippingAddress
    } = useForm<AddressDetailVm>({resolver:yupResolver(addressShippingSchema)})
    const {
        handleSubmit :handleSubmitOrder,
        register:registerOrder,
        formState: { errors:errorsOrder },
        watch:watchOrder,
    } = useForm<OrderDetailVm>();



    const handleCloseModalShippingAddress = ()=>{
        setIsShowModalShippingAddress(false)
    }
    const handleSelectedShippingAddress = (address:AddressDetailVm)=>{
        setIsAddShippingAddress(false)
        setShippingAddress(address);
        setCurrentAddressId(address.id)
    }


    const onSubmitShippingAddress = async (data:AddressDetailVm,event:any)=>{
        const newAddress = await performCreateUserAddress(data);
        setShippingAddress(newAddress);
        setIsAddShippingAddress(false)

    }
    const performCreateUserAddress =async (address:AddressDetailVm):Promise<AddressDetailVm>=>{
        await addressShippingSchema.validate(address).catch(error=> console.log(error))
        let addressCreated ;
        try {
            const {addressVm : newAddress} = await userAddressService.createCustomerAddress(address as AddressPostVm)
            addressCreated = newAddress;
        }catch (error){

            console.log(error)
        }
        let addressDetailCreated : AddressDetailVm = addressCreated as AddressDetailVm;
        try {
            if (addressDetailCreated.id != null) {
                addressDetailCreated = await addressService.getAddressById(addressDetailCreated.id);
            }

        }catch (error){
            console.log(error)
        }
        return addressDetailCreated;

    }


    useEffect(() => {
        userAddressService.getDetailAddresses()
            .then((responseAddressDefault)=>{
                setShippingAddress(responseAddressDefault);
            }).catch((error)=>{
                setIsAddShippingAddress(true)
        })
    }, []);
    useEffect(() => {
        if(id){
            orderService.getCheckoutById(parseInt(id as string))
                .then((responseCheckoutVm)=>{
                    setCheckout(responseCheckoutVm)
                })
        }
    }, [id]);


    return (
        <Container>
            <section className="checkout spad">
                {isShowSpinner ? <Loading></Loading> :''}
                <div className="container">
                    <div className="checkout__form">
                        <form onSubmit={(event) => {}}>
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
                                                    setIsShowModalShippingAddress(true);
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
                                                handleSubmit={handleSubmitShippingAddress(onSubmitShippingAddress)}
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
                                                register={registerOrder}
                                                placeholder="Notes about your order, e.g. special notes for delivery."
                                                error={errorsOrder.note?.message}
                                            />
                                        </div>
                                        <div className="checkout__input">
                                            <Input
                                                type="text"
                                                labelText="Email"
                                                fieldName="email"
                                                register={registerOrder}
                                                defaultValue={checkout?.email}
                                                error={errorsOrder.email?.message}
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
                            isShow={isShowModalShippingAddress}
                            handleCloseModel={handleCloseModalShippingAddress}
                            handleSelectAddress={handleSelectedShippingAddress}
                            currentAddressId = {currentAddressId}

                        />

                    </div>
                </div>
            </section>
        </Container>
    )


}
export default Checkout;