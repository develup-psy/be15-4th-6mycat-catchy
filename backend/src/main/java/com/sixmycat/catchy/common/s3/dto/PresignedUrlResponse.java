package com.sixmycat.catchy.common.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlResponse {
    private String presignedUrl;
    private String fileKey; // 실제 S3에 저장될 경로
}
