import { AddressVm } from "@/models/address/AddressVm";
import { FC } from "react";
import { FieldErrors, useForm, UseFormRegister } from "react-hook-form";
import Input from "../item/input";

type Props = {
    // để đăng ký
    register: UseFormRegister<AddressVm>,
    // submit
    handleSubmit: ()=> void,
    errors: FieldErrors<AddressVm>,
    address? : AddressVm| undefined,
    error: FieldErrors<AddressVm>
    
}
const AddressForm : FC<Props> = (
    {
        errors,
        handleSubmit,
        register,
        address
    }
)=>{
    
    return (
        <div >
        <div className="row">
          <div className="col-lg-6">
            <div className="checkout__input">
              <Input
                labelText="Contact name"
                register={register}
                field="contactName"
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
                field="phoneNumber"
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
                field="countryId"
                placeholder="Select country"
                options={countries}
                register={register}
                registerOptions={{
                  required: { value: true, message: 'Please select country' },
                  onChange: onCountryChange,
                }}
                error={errors.countryId?.message}
                defaultValue={address?.countryId}
              />
            </div>
          </div>
          <div className="col-lg-8">
            <div className="checkout__input">
              <div className="mb-3">
                <OptionSelect
                  labelText="State Or Province"
                  register={register}
                  field="stateOrProvinceId"
                  options={statesOrProvinces}
                  placeholder="Select state or province"
                  defaultValue={address?.stateOrProvinceId}
                  registerOptions={{
                    required: { value: true, message: 'Please select state or province' },
                    onChange: onStateOrProvinceChange,
                  }}
                />
              </div>
            </div>
          </div>
        </div>
        <div className="row">
          <div className="col-lg-6">
            <div className="checkout__input">
              <Input
                labelText="City"
                register={register}
                field="city"
                placeholder="Please skip this field if you are not in a city"
                defaultValue={address?.city}
              />
            </div>
          </div>
          <div className="col-lg-6">
            <div className="checkout__input">
              <OptionSelect
                labelText="District"
                register={register}
                field="districtId"
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
          </div>
        </div>
        <div className="row">
          <div className="col-lg-10">
            <div className="checkout__input">
              <Input
                labelText="Address"
                register={register}
                field="addressLine1"
                registerOptions={{
                  required: { value: true, message: 'This feild is required' },
                }}
                defaultValue={address?.addressLine1}
              />
            </div>
          </div>
          <div className="col-lg-2">
            <div className="checkout__input">
              <Input
                labelText="Zip code"
                register={register}
                field="zipCode"
                registerOptions={{
                  required: { value: true, message: 'This feild is required' },
                }}
                defaultValue={address?.zipCode}
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
              {buttonText ?? 'Create'}
            </button>
          </div>
        </div>
      </div>
    </>
        
    )

}
export default AddressForm;