package com.lexuancong.media.dto;

import com.lexuancong.media.model.Image;

public record ImageGetResponse(
        Long id ,
        String description,
        String fileName,
        String imageType
) {
    public static ImageGetResponse fromImage(Image image){
        return new ImageGetResponse(image.getId(), image.getDescription(), image.getFileName(), image.getImageType());
    }
}
