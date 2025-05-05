package com.lexuancong.search.viewmodel;

import com.lexuancong.search.model.Product;

public record ProductPreviewVm(
        Long id,String name,String slug,Double price ,Long avatarId) {
    public static ProductPreviewVm fromModel(Product product) {
        return new ProductPreviewVm(product.getId(),product.getName(),product.getSlug(),
                product.getPrice(),product.getAvatarImageId());
    }


}
