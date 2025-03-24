package com.lexuancong.media.service;

import com.lexuancong.media.repository.MediaRepository;
import org.springframework.stereotype.Service;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;
    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }



}
