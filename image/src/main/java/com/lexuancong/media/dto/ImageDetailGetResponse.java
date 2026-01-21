package com.lexuancong.media.dto;

import java.io.InputStream;

public record ImageDetailGetResponse(
        Long id ,
        String description,
        String fileName,
        String imageType,
        InputStream content
) {
}
