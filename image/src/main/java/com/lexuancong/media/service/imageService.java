package com.lexuancong.media.service;

import com.lexuancong.media.config.FilesystemConfig;
import com.lexuancong.media.model.Image;
import com.lexuancong.media.repository.ImageRepository;
import com.lexuancong.media.viewmodel.ImageDetailVm;
import com.lexuancong.media.viewmodel.ImagePostVm;
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
    private final FilesystemConfig filesystemConfig;
    public imageService(ImageRepository imageRepository, FilesystemConfig filesystemConfig) {
        this.imageRepository = imageRepository;
        this.filesystemConfig = filesystemConfig;
    }

    public Image create(ImagePostVm imagePostVm)  {
        Image image = imagePostVm.toModel();
        try {
            // dữ liệu của file được  the hien duoi dang file
            String filePath = this.saveFileInFilesystem(image.getFileName() , imagePostVm.file().getBytes());
            image.setFilePath(filePath);
            imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String saveFileInFilesystem(String fileName, byte[] contentFile){
        File directory = new File(filesystemConfig.getDirectory());
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
        Path path = Paths.get(filesystemConfig.getDirectory(), fileName).toAbsolutePath().normalize();
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
        // xóa file trong systemt trước
        Path pathFile = this.buildFilePath(image.getFileName());
        try {
            Files.deleteIfExists(pathFile);
            imageRepository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public ImageDetailVm getImageById(Long id){
        // dựa vào file path ddeer lays trong file sytemt
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        Path path = Paths.get(image.getFilePath());
        this.checkIsExitedPath(path);
        InputStream fileContent = null;
        try {
            fileContent = Files.newInputStream(path);
            return new ImageDetailVm(image.getId() ,image.getDescription() ,
                    image.getFileName(),image.getImageType(),fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void checkIsExitedPath(Path path) {
        if(!Files.exists(path)) {
            throw new RuntimeException("Image not found");
        }
    }








}
