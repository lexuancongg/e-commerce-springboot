import {FC, useState} from "react";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import Modal from "react-bootstrap/Modal";
import CardAddress from "@/components/address/cardAddress";
import AddressCard from "@/components/order/AddressCard";

type Props = {
    isShow: boolean,
    handleCloseModel: () => void,
    handleSelectAddress: () => void,
}
const ModalAddressList: FC<Props> = ({
                                         handleCloseModel,
                                         isShow,
                                         handleSelectAddress
                                     }) => {
    const [addresses,setAddresses] = useState<AddressDetailVm[]>([])

    return (
        <Modal show={isShow} onHide={() => handleCloseModel()} size="lg" centered>
            <Modal.Header closeButton>
                <Modal.Title className="text-dark fw-bold">Select address</Modal.Title>
            </Modal.Header>
            <Modal.Body style={{ minHeight: '500px' }}>
                <div className="body">
                    <div className="row">
                        {addresses.length == 0 ? (
                            <div className="mx-2">Please add your address in your profile</div>
                        ) : (
                            addresses.map((address) => (
                                <div
                                    className="col-lg-6 mb-2"
                                    onClick={() => {
                                    }}
                                    key={address.id}
                                >
                                    <AddressCard address={address} isSelected={false} />
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </Modal.Body>
        </Modal>
    );

}
export default ModalAddressList;