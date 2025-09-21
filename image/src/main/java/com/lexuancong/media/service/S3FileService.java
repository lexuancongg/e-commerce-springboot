package com.lexuancong.media.service;

import com.lexuancong.media.model.Image;
import com.lexuancong.media.repository.ImageRepository;
import com.lexuancong.media.viewmodel.ImageDetailVm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3FileService {

    private final S3Client s3Client;
    private final ImageRepository imageRepository;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    private final Region region = Region.AP_SOUTHEAST_1;

    public String uploadFile(String fileName, byte[] content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl(ObjectCannedACL.PUBLIC_READ) // hoặc private
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    public ImageDetailVm getImageById(Long id) {
        // lấy thông tin ảnh từ DB
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // tạo pre-signed URL
        try (S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(image.getFilePath()) // filePath lưu trong DB
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(5))
                    .build();

            URL url = presigner.presignGetObject(presignRequest).url();

            return new ImageDetailVm(
                    image.getId(),
                    image.getDescription(),
                    image.getFileName(),
                    image.getImageType(),
                    null
            );

        } catch (Exception e) {
            throw new RuntimeException("Cannot generate presigned URL", e);
        }
    }



}
