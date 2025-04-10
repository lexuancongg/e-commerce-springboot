package com.lexuancong.product.service.internal;

import com.lexuancong.product.viewmodel.image.ImageVm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {
    public ImageVm getImageById (Long id){
        return null;
    }

    public List<ImageVm> getImageByIds (List<Long> ids){
        return new ArrayList<>();
    }
}
