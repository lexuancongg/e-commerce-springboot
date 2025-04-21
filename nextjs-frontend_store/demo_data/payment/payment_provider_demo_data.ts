import {PaymentProvider} from "@/models/payment/PaymentProvider";

export const payment_provider_demo_data : PaymentProvider[] =  [
    {
        id: 3,
        name: "VNPAY",
        configureUrl: "https://vnpay.vn/config",
        additionalSettings: JSON.stringify({
            merchantCode: "VNP123456",
            hashSecret: "s3cr3tKeyHere",
            returnUrl: "https://your-site.com/vnpay-return"
        })
    }
];