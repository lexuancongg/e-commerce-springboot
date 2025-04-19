package com.lexuancong.media.controller;

import com.lexuancong.media.service.MediaService;
import com.lexuancong.media.viewmodel.ImagePostVm;
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
public class ImageController {
    private final MediaService mediaService;

    // gởi lên bằng submit form
    @PostMapping(value = "/management/medias", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    // ModelAttribute : ánh xạ data từ form vào obj như requestBody
    public ResponseEntity<Object> create(@ModelAttribute @Valid ImagePostVm imagePostVm){

    }

}
