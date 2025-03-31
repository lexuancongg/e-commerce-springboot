import { error } from "console";
import { HTMLInputTypeAttribute } from "react";
import { FieldValues, Path, RegisterOptions, UseFormRegister } from "react-hook-form";

type Props<T extends FieldValues>  = {
    // nội dung của input
    labelText: string,
    // field để đăng ký cho input , path chi cho lấy key hợp lệ của T(object đại diện cho dữ liệu của form)
    field : Path<T>,
    register: UseFormRegister<T>,
    type ?: HTMLInputTypeAttribute,
    registerOptions: RegisterOptions<T>,
    defaultValue?: string,
    disabled ?: boolean,
    placehoder ?: string,
    error? : string
    
}
const Input = <T extends FieldValues> (
    {
        field,
        labelText,
        register,
        registerOptions,
        defaultValue,
        disabled = false,
        placehoder,
        type,
        error
    } : Props<T>
)=>{
    return (
        <div className="mb-3">
        <label className="form-label" htmlFor={field}>
          {labelText} {registerOptions?.required && <span className="text-danger">*</span>}
        </label>
        <input
          type={type}
          id={field}
          className={`form-control ${error ? 'border-danger' : ''}`}
          {...register(field, registerOptions)}
          defaultValue={defaultValue}
          disabled={disabled}
          placeholder={placehoder}
        />
        <p className="error-field mt-1 text-danger">lỗi</p>
      </div>
    )
}
export default Input;