package com.lexuancong.media.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "image")
@Builder
@NoArgsConstructor
@AllArgsConstructor

// file chúng ta sẽ luwu vào system còn db lưu vài thông tin file
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description ;

    private String fileName;

    private String filePath;

    private String imageType;
}
