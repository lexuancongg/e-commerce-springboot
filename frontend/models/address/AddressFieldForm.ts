export type AddressFieldForm ={
    contactName : string,
    phoneNumber: string,
    specificAddress: string,
    districtId: number,
    provinceId: number,
    countryId:number,
    // sau này có thể đưa về thêm tên
    districtName ?: string,
    provinceName?: string,
    countryName?: string,

}