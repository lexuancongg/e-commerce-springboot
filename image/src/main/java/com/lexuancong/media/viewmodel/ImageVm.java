package com.lexuancong.media.viewmodel;

import com.lexuancong.media.model.Image;

public record ImageVm(
        Long id ,
        String description,
        String fileName,
        String imageType
) {
    public static ImageVm fromModel(Image image){
        return new ImageVm(image.getId(), image.getDescription(), image.getFileName(), image.getImageType());
    }
}
