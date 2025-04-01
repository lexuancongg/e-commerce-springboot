'use client'
import { AddressVm } from "@/models/address/AddressVm";
import React, { FC, useEffect, useState } from "react";
import { FieldErrors, set, useForm, UseFormRegister, UseFormSetValue } from "react-hook-form";
import Input from "../item/input";
import OptionSelect from "../item/optionSelect";
import { CountryVm } from "@/models/address/CountryVm";
import { ProvinceVm } from "@/models/address/ProvinceVm";
import { DistrictVm } from "@/models/address/DistrictVm";
import addressService from "@/services/address/addressService";
import { useRouter } from "next/router";

type Props = {
  // để đăng ký
  register: UseFormRegister<AddressVm>,
  // submit
  handleSubmit: () => void,
  errors: FieldErrors<AddressVm>,
  // trường hợp có nếu update , còn undefine nếu create
  address?: AddressVm | undefined,
  // xác định tên nút là create hay update
  buttonText?: string,
  setValue: UseFormSetValue<AddressVm>;
}
const AddressForm: FC<Props> = (
  {
    errors,
    handleSubmit,
    register,
    address,
    buttonText,
    setValue
  }
) => {


  // dành cho update 
  const router = useRouter();
  const { id } = router.query; // Lấy giá trị 'id' từ query params

  const [countries, setCountries] = useState<CountryVm[]>([]);
  const [provinces, setProvinces] = useState<ProvinceVm[]>([]);
  const [districts, setDistricts] = useState<DistrictVm[]>([]);
  // đầu tiên phải lấy được danh sách countru
  useEffect(() => {
    addressService.getCountries()
      .then((resCountries) => {
        setCountries(resCountries);
      })
  }, [])

  // trường hợp useForm hiển thị cho edit => phải lấy được district và province tương ứng
  useEffect(() => {
    if (address) {
      // lấy province theo country
      addressService.getProvinces(address.countryId)
        .then((resProvinces) => {
          setProvinces(resProvinces);
        })
      addressService.getDistricts(address.provinceId)
        .then((resDistricts) => {
          setDistricts(resDistricts);
        })
    }
  }, [id])

  // sự kiện khi thay đổi onchange của countriId => cập nhật lại province
  const onchangeCountry = async (event: React.ChangeEvent<HTMLSelectElement>) => {
    setValue('countryName', event.target.selectedOptions[0].text)
    // cập nhật lại giá trị provinces 
    const countryId = parseInt(event.target.value);
    addressService.getProvinces(countryId)
      .then((resProvinces) => {
        setProvinces(resProvinces)
      })
  }

  // sự kiện khi thay đổi  province => cập nhật lại district
  const onchangeProvince = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setValue('provinceName', event.target.selectedOptions[0].text)
    const provinceId = parseInt(event.target.value);
    addressService.getDistricts(provinceId)
      .then((resDistricts) => {
        setDistricts(resDistricts);
      })
  }




  return (
    <div>
      <div className="row">
        <div className="col-lg-6">
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
        <div className="col-lg-6">
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
      <div className="row">
        <div className="col-lg-4">
          <OptionSelect
            labelText="Country"
            fieldName="countryId"
            placeholder="Select country"
            defaultValue={address?.countryId}
            options={countries}
            register={register}
            registerOptions={{
              required: { value: true, message: 'Please select country' },
              onChange: onchangeCountry ,
            }}
            error={errors.countryId?.message}
          />
        </div>
        <div className="col-lg-8">
          <OptionSelect
            labelText="Province"
            register={register}
            fieldName="provinceId"
            options={provinces}
            placeholder="Select state or province"
            defaultValue={address?.provinceId}
            registerOptions={{
              required: { value: true, message: 'Please select state or province' },
              onChange: onchangeProvince,
            }}
          />
        </div>
      </div>
      <div className="row">
        <div className="col-lg-5">
          <OptionSelect
            labelText="District"
            register={register}
            fieldName="districtId"
            options={districts}
            placeholder="Select district"
            defaultValue={address?.districtId}
            registerOptions={{
              required: { value: true, message: 'Please select district' },
              onChange: (event: any) => {
                setValue('districtName', event.target.selectedOptions[0].text);
              },
            }}
          />

        </div>
        <div className="col-lg-7">
          <Input
            labelText="specificAddress"
            register={register}
            fieldName="specificAddress"
            defaultValue={address?.districtId}
            registerOptions={{
              required: { value: true, message: 'Please input  specificAddress' },
             
            }}
          />

        </div>
      </div>

      <div className="row mt-2">
        <div className="col-lg-12 d-flex justify-content-end">
          <button
            type="button"
            className="btn btn-outline-primary fw-bold px-2 py-2"
            onClick={handleSubmit}
          >
            {buttonText}
          </button>
        </div>
      </div>
    </div>


  )

}
export default AddressForm;