import { FieldValues, Path, RegisterOptions, UseFormRegister } from "react-hook-form";

type Option = {
    id: number,
    name:string
}
type Props<T extends FieldValues> = {
    labelText: string,
    fieldName : Path<T>,
    register : UseFormRegister<T>,
  registerOptions?: RegisterOptions,
  options: Option[],
  defaultValue?: string ,
  placeholder?: string;
  disabled?: boolean;
}

const OptionSelect = ()=>{

}
export default OptionSelect;