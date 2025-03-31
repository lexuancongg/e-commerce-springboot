import { FieldValues, Path, RegisterOptions, UseFormRegister } from "react-hook-form";

type Option = {
    id: number,
    name:string
}
type Props<T extends FieldValues> = {
    labelText: string,
    fieldName : Path<T>,
    register : UseFormRegister<T>,
  registerOptions?: RegisterOptions<T>,
  options: Option[],
  defaultValue?: string ,
  placeholder?: string,
  disabled?: boolean,
  error?: string
}

const OptionSelect =<T extends FieldValues> (
  {
    fieldName,
    labelText,
    options,
    register,
    defaultValue,
    disabled,
    placeholder,
    registerOptions,
    error
  }:Props<T>
)=>{
  return (
    <div className="mb-3">
    <label className="form-label" htmlFor={`select-option-${fieldName}`}>
      {labelText} {registerOptions?.required && <span className="text-danger">*</span>}
    </label>
    <select
      id={`select-option-${fieldName}`}
      className={`form-select ${error ? 'border-danger' : ''}`}
      defaultValue={defaultValue || ''}
      {...register(fieldName, registerOptions)}
      disabled={disabled}
    >
      <option disabled hidden value="">
        {placeholder || 'Select ...'}
      </option>
      {(options || []).map((option) => (
        <option value={option.id} key={option.id}>
          {option.name}
        </option>
      ))}
    </select>
    <p className="error-field mt-1">{error}</p>
  </div>
  )

}
export default OptionSelect;