package com.lexuancong.image.service;

import com.lexuancong.image.config.FilesystemProperties;
import com.lexuancong.image.model.Image;
import com.lexuancong.image.repository.ImageRepository;
import com.lexuancong.image.dto.ImageDetailResponse;
import com.lexuancong.image.dto.ImageCreateRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class imageService {
    private final ImageRepository imageRepository;
    private final FilesystemProperties filesystemProperties;
    public imageService(ImageRepository imageRepository, FilesystemProperties filesystemProperties) {
        this.imageRepository = imageRepository;
        this.filesystemProperties = filesystemProperties;
    }

    public Image create(ImageCreateRequest imageCreateRequest)  {
        Image image = imageCreateRequest.toImage();
        try {
            // dữ liệu của file được  the hien duoi dang file
            String filePath = this.saveFileInFilesystem(image.getFileName() , imageCreateRequest.file().getBytes());
            image.setFilePath(filePath);
            imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    public String saveFileInFilesystem(String fileName, byte[] contentFile){
        File directory = new File(filesystemProperties.getDirectory());
        this.checkIsExitedDirectory(directory);
        this.validateFileName(fileName);
        Path filePath = this.buildFilePath(fileName);
        try {
            Files.write(filePath, contentFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath.toString();


    }

    private Path buildFilePath(String fileName) {
        Path path = Paths.get(filesystemProperties.getDirectory(), fileName).toAbsolutePath().normalize();
        return path;
    }

    private void validateFileName(String fileName) {
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            // bắn ra ngoại lệ
        }
    }

    private void checkIsExitedDirectory(File directory) {
        if(!directory.exists()) {
            directory.mkdirs();
        }
    }




    public void delete(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        Path pathFile = this.buildFilePath(image.getFileName());
        try {
            Files.deleteIfExists(pathFile);
            imageRepository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public ImageDetailResponse getImageById(Long id){
        // dựa vào file path ddeer lays trong file sytemt
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        Path path = Paths.get(image.getFilePath());
        this.checkIsExitedPath(path);
            return new ImageDetailResponse(image.getId() ,
                    image.getFileName(),"");

    }

    private void checkIsExitedPath(Path path) {
        if(!Files.exists(path)) {
            throw new RuntimeException("Image not found");
        }
    }








}
