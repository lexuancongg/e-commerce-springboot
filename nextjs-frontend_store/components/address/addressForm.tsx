'use client'
import { AddressVm } from "@/models/address/AddressVm";
import { FC, useState } from "react";
import { FieldErrors, useForm, UseFormRegister } from "react-hook-form";
import Input from "../item/input";
import OptionSelect from "../item/optionSelect";
import { CountryVm } from "@/models/address/CountryVm";
import { ProvinceVm } from "@/models/address/ProvinceVm";
import { DistrictVm } from "@/models/address/DistrictVm";

type Props = {
  // để đăng ký
  register: UseFormRegister<AddressVm>,
  // submit
  handleSubmit: () => void,
  errors: FieldErrors<AddressVm>,
  // trường hợp có nếu update , còn undefine nếu create
  address?: AddressVm | undefined,
  // xác định tên nút là create hay update
  buttonText?: string

}
const AddressForm: FC<Props> = (
  {
    errors,
    handleSubmit,
    register,
    address,
    buttonText
  }
) => {
  // dành cho update 
  const [countries, setCountries] = useState<CountryVm[]>([]);
  const [provinces,setProvinces] = useState<ProvinceVm[]>([]);
  const [districts, setDistricts]= useState<DistrictVm[]>([]);

  return (
    <div >
      <div className="row">
        <div className="col-lg-6">
          <div className="checkout__input">
            <Input
              labelText="Contact name"
              register={register}
              fieldName="contactName"
              registerOptions={{
                required: { value: true, message: 'This feild is required' },
              }}
              defaultValue={address?.contactName}
            />
          </div>
        </div>
        <div className="col-lg-6">
          <div className="checkout__input">
            <Input
              labelText="Phone number"
              register={register}
              fieldName="phoneNumber"
              registerOptions={{
                required: { value: true, message: 'This feild is required' },
              }}
              defaultValue={address?.phoneNumber}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-lg-4">
          <div className="checkout__input">
            <OptionSelect
              labelText="Country"
              fieldName="countryId"
              placeholder="Select country"
              options={ countries}
              register={register}
              registerOptions={{
                required: { value: true, message: 'Please select country' },
                // onChange: onCountryChange,
              }}
              error={errors.countryId?.message}
            // defaultValue={address?.countryId}
            />
          </div>
        </div>
        <div className="col-lg-8">
          <div className="checkout__input">
            <div className="mb-3">
              <OptionSelect
                labelText="State Or Province"
                register={register}
                fieldName="provinceId"
                options={ provinces}
                placeholder="Select state or province"
                defaultValue={address?.provinceName }
                registerOptions={{
                  required: { value: true, message: 'Please select state or province' },
                  // onChange: onStateOrProvinceChange,
                }}
              />
            </div>
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-lg-6">
          <div className="checkout__input">
            <OptionSelect
              labelText="District"
              register={register}
              fieldName="districtId"
              options={districts }
              placeholder="Select district"
              // defaultValue={address?.districtId}
              registerOptions={{
                required: { value: true, message: 'Please select district' },
                onChange: (event: any) => {
                  // setValue('districtName', event.target.selectedOptions[0].text);
                },
              }}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-lg-10">
          <div className="checkout__input">
            <Input
              labelText="Address"
              register={register}
              fieldName="specificAddress"
              registerOptions={{
                required: { value: true, message: 'This feild is required' },
              }}
            // defaultValue={address?.addressLine1}
            />
          </div>
        </div>

      </div>
      <div className="row mt-2">
        <div className="col-lg-12 d-flex justify-content-end">
          <button
            type="button"
            className="btn btn-outline-primary fw-bold px-2 py-2"
            onClick={handleSubmit}
          >
            {/* {buttonText ?? 'Create'} */}
          </button>
        </div>
      </div>
    </div>


  )

}
export default AddressForm;