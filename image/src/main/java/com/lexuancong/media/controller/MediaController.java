package com.lexuancong.media.controller;

import com.lexuancong.media.service.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//  Preflight Request (OPTIONS) cache kết quả  Preflight Request lân đầu trong 1h , => lần sau không cần gởi nữa
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping(value = "/management/medias", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> create(@ModelAttribute @Valid )

}
