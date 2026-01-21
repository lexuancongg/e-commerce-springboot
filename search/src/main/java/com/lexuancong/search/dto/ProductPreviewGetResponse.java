package com.lexuancong.search.dto;

import com.lexuancong.search.model.Product;

public record ProductPreviewGetResponse(
        Long id,String name,String slug,Double price ,Long avatarId) {
    public static ProductPreviewGetResponse fromModel(Product product) {
        return new ProductPreviewGetResponse(product.getId(),product.getName(),product.getSlug(),
                product.getPrice(),product.getAvatarImageId());
    }


}
