package com.sixmycat.catchy.feature.rekognition.command.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageModerationService {

    private final RekognitionClient rekognitionClient;

    public List<Label> detectLabels(String bucketName, String objectKey) {
        S3Object s3Object = S3Object.builder()
                .bucket(bucketName)
                .name(objectKey)
                .build();

        Image image = Image.builder()
                .s3Object(s3Object)
                .build();

        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(image)
                .maxLabels(10)
                .minConfidence(70F)
                .build();

        DetectLabelsResponse result = rekognitionClient.detectLabels(request);

        return result.labels();
    }
}