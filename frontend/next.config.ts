import type { NextConfig } from "next";

// config file cho nextjs
const nextConfig: NextConfig = {
    reactStrictMode :true,
    // cấu hình cho phép tải hình ảnh bên ngoài,
    experimental: {
    },
    images: {
        remotePatterns:[
            {
                protocol: 'https',
                hostname: 's3.amazonaws.com',
                port: '',
                pathname: '/my-bucket/**',
                search: '',
            },
        ]
    }
};

export default nextConfig;
