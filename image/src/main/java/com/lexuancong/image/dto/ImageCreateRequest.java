package com.lexuancong.image.dto;

import com.lexuancong.image.model.Image;
import com.lexuancong.image.validation.ValidateTypeFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public record ImageCreateRequest(
        @ValidateTypeFile(allowedTypes = {"image/jpeg", "image/png", "image/gif"}) MultipartFile file
) {
    public Image toImage(){
        Image image = new Image();
        image.setImageType(this.file.getContentType());
            image.setFileName(file.getOriginalFilename());
        return image;

}

}
