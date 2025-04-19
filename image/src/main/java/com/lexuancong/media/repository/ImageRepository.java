package com.lexuancong.media.repository;

import com.lexuancong.media.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
