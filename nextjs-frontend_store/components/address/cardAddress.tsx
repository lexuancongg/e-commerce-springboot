import { AddressDetailVm } from "@/models/address/AddressDetailVm";
import Link from "next/link";
import { FC } from "react";
import { FaTrash } from "react-icons/fa";
import { FiEdit } from "react-icons/fi";
import { HiCheckCircle } from "react-icons/hi";
import { TiContacts } from "react-icons/ti";
type Props = {
  address: AddressDetailVm,
  handleChooseAddressDefault: (addressId: number) => void,
  handleChooseDeleteAddress: (addressId: number) => void
}
const CardAddress: FC<Props> = (
  {
    address,
    handleChooseAddressDefault,
    handleChooseDeleteAddress
  }
) => {
  return (
    <div className="col-span-4  w-1/2 p-2" key={address.id}>
      <div className="flex bg-red-500 rounded-lg overflow-hidden shadow-md">
        <div className="flex items-center justify-center w-24 bg-blue-500">
          <TiContacts className="text-white text-5xl" />
        </div>
        <div className="p-4 w-full bg-red-500 text-white">
          {address.isActive == true && (
              <div className="m-2" style={{ float: 'right' }}>
                <div
                    style={{
                      width: '15px',
                      height: '15px',
                      borderRadius: '50%',
                      background: '#0eea5d',
                    }}
                ></div>
              </div>
          )}
          <div className="text-sm">Contact name: {address.contactName}</div>
          <div className="text-sm break-words">Address: {address.specificAddress}</div>
          <div className="text-sm">Phone number: {address.phoneNumber}</div>
          <div className="flex justify-end space-x-3 mt-3">
            <HiCheckCircle className="cursor-pointer text-green-300" title="Active" />
            <Link href={`/address/${address.id}/edit`}>
              <FiEdit className="cursor-pointer text-blue-300" title="Edit" />
            </Link>
            <FaTrash className="cursor-pointer text-red-300" title="Delete"
              onClick={() => handleChooseDeleteAddress(address.id || 0)}
            />
          </div>
        </div>
      </div>
    </div>
  );
};
export default CardAddress;