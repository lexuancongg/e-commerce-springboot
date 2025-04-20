package com.lexuancong.media.viewmodel;

import java.io.InputStream;

public record ImageDetailVm(
        Long id ,
        String description,
        String fileName,
        String imageType,
        InputStream content
) {
}
