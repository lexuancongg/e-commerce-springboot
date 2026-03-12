package com.lexuancong.image.dto;

import com.lexuancong.image.model.Image;

public record ImageResponse(
        Long id ,
        String fileName,
        String imageType
) {
    public static ImageResponse fromImage(Image image){
        return new ImageResponse(image.getId(), image.getFileName(), image.getImageType());
    }
}
