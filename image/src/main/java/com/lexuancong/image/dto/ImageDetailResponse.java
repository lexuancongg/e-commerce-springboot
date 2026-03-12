package com.lexuancong.image.dto;

import java.io.InputStream;

public record ImageDetailResponse(
        Long id ,
        String fileName,
        String url
) {
}
