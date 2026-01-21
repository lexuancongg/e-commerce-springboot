package com.lexuancong.media.controller;

import com.lexuancong.media.model.Image;
import com.lexuancong.media.service.imageService;
import com.lexuancong.media.dto.ImageDetailGetResponse;
import com.lexuancong.media.dto.ImageCreateRequest;
import com.lexuancong.media.dto.ImageGetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//  Preflight Request (OPTIONS) cache kết quả  Preflight Request lân đầu trong 1h , => lần sau không cần gởi nữa
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ImageController {
    private final imageService imageService;

    // gởi lên bằng submit form
    @PostMapping(value = "/management/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    // ModelAttribute : ánh xạ data từ form vào obj như requestBody
    public ResponseEntity<ImageGetResponse> create(@ModelAttribute @Valid ImageCreateRequest imageCreateRequest){
        Image imageSaved = this.imageService.create(imageCreateRequest);
        return ResponseEntity.ok(ImageGetResponse.fromImage(imageSaved));

    }

    @DeleteMapping("/management/images/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.imageService.delete(id);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/customer/images/{id}")
    public ResponseEntity<ImageDetailGetResponse> get(@PathVariable Long id){
        // chúng ta chỉ mới bt  id => cần file name để lấy trong filesystem
        return ResponseEntity.ok(this.imageService.getImageById(id));
    }



}
