import { error } from "console";
import { HTMLInputTypeAttribute } from "react";
import { FieldValues, Path, RegisterOptions, UseFormRegister } from "react-hook-form";

type Props<T extends FieldValues> = {
  // nội dung của input
  labelText: string,
  // field để đăng ký cho input , path chi cho lấy key hợp lệ của T(object đại diện cho dữ liệu của form)
  fieldName: Path<T>,
  register: UseFormRegister<T>,
  type?: HTMLInputTypeAttribute,
  registerOptions: RegisterOptions<T>,
  defaultValue?: string | number,
  disabled?: boolean,
  placehoder?: string,
  error?: string

}
const Input = <T extends FieldValues>(
  {
    fieldName,
    labelText,
    register,
    registerOptions,
    defaultValue,
    disabled = false,
    placehoder,
    type,
    error
  }: Props<T>
) => {
  return (
    <div className="mb-3">
      <label className="form-label" htmlFor={fieldName}>
        {labelText} {registerOptions?.required && <span className="text-danger">*</span>}
      </label>
      <input
        type={type}
        id={fieldName}
        className={` w-full h-12 pl-5 text-base text-gray-900 border border-gray-300 rounded-md ${error ? 'border-danger' : ''}`}
        {...register(fieldName, registerOptions)}
        defaultValue={defaultValue}
        disabled={disabled}
        placeholder={placehoder}
      />
      <p className="error-field mt-1 text-danger">{error}</p>
    </div>
  )
}
export default Input;