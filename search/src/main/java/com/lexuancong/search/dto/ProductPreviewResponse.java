package com.lexuancong.search.dto;

import com.lexuancong.search.model.Product;

public record ProductPreviewResponse(
        Long id,String name,String slug,Double price ,Long avatarId) {
    public static ProductPreviewResponse fromProduct(Product product) {
        return new ProductPreviewResponse(product.getId(),product.getName(),product.getSlug(),
                product.getPrice(),product.getAvatarImageId());
    }


}
