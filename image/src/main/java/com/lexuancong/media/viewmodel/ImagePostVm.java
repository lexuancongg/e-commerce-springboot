package com.lexuancong.media.viewmodel;

import com.lexuancong.media.model.Image;
import com.lexuancong.media.validation.ValidateTypeFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public record ImagePostVm(
        String description,
        @ValidateTypeFile(allowedTypes = {"image/jpeg", "image/png", "image/gif"}) MultipartFile file,
        String fileName
) {
    public Image toModel(){
        Image image = new Image();
        image.setDescription(description);
        image.setImageType(this.file.getContentType());
        if(StringUtils.hasText(fileName)){
            image.setFileName(fileName);
        }else{
            image.setFileName(file.getOriginalFilename());
        }
        return image;

}

}
