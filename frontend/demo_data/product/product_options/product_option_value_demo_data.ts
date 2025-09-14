import {SpecificProductVariantVm} from "@/models/product/specific_variant/SpecificProductVariantGetVm";


const variants: SpecificProductVariantVm[] = [
    {
        id: 1,
        productOptionId: 101,
        productId: 1001,
        productOptionName: "Color",
        productOptionValue: "Red",
    },
    {
        id: 2,
        productOptionId: 102,
        productId: 1001,
        productOptionName: "Size",
        productOptionValue: "M",
    },
    {
        id: 3,
        productOptionId: 101,
        productId: 1002,
        productOptionName: "Color",
        productOptionValue: "Blue",
    },
];

export default  variants;