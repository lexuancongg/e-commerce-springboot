import { HTMLInputTypeAttribute } from "react";
import { FieldValues, Path, RegisterOptions, UseFormRegister } from "react-hook-form";

type Props<T extends FieldValues>  = {
    // nội dung của input
    labelText: string,
    // field để đăng ký cho input , path chi cho lấy key hợp lệ của T(object đại diện cho dữ liệu của form)
    field : Path<T>,
    register: UseFormRegister<T>,
    type ?: HTMLInputTypeAttribute,
    registerOptions: RegisterOptions,
    defaultValue?: string,
    disabled ?: boolean,
    placehoder ?: string
    
}
const Input = <T extends FieldValues> (
    {
    } : Props<T>
)=>{

}
export default Input;