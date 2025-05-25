package com.sixmycat.catchy.common.s3;

import com.sixmycat.catchy.common.s3.dto.S3UploadResult;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    public S3UploadResult uploadFile(MultipartFile file, String dirName) {
        String fileKey = generateFileName(dirName, file.getOriginalFilename());

        if (!file.getContentType().startsWith("image/")) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE); // 예: image/png, image/jpeg만 허용
        }

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        String url =  "https://" + cloudFrontDomain + "/" + fileKey;
        return new S3UploadResult(fileKey, url);
    }

    public List<String> uploadFiles(List<MultipartFile> files, String dirName) {
        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadedUrls.add(uploadFile(file, dirName).url());
        }
        return uploadedUrls;
    }

    private String generateFileName(String dirName, String originalName) {
        return dirName + "/" + UUID.randomUUID() + "-" + originalName;
    }

    public void deleteFile(String fileKey) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new RuntimeException("S3 파일 삭제 실패: " + fileKey, e);
        }
    }

}