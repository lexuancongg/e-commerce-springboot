package com.lexuancong.media.viewmodel;

import com.lexuancong.media.validation.ValidateTypeFile;
import org.springframework.web.multipart.MultipartFile;

public record ImagePostVm(
        String  description,
        @ValidateTypeFile(allowedTypes = {"image/jpeg", "image/png", "image/gif") MultipartFile file,
        // lu trữ name rõ ràng trong hệ thống
        String fileName
                          ) {

                          }

}
