package com.sixmycat.catchy.feature.rekognition.command.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RekognitionService {

    private final RekognitionClient rekognitionClient;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    private static final List<String> FORBIDDEN_LABELS = List.of(
            "dog", "puppy", "canine", "bulldog", "poodle"
    );

    private static final List<String> CAT_LABELS = List.of(
            "cat", "kitten", "feline", "tabby", "pet cat"
    );

    /**
     * S3 이미지 키를 기반으로 Rekognition 라벨 분석 수행
     */
    public List<Label> detectLabels(String imageKey) {
        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(Image.builder()
                        .s3Object(S3Object.builder()
                                .bucket(bucketName)
                                .name(imageKey)
                                .build())
                        .build())
                .maxLabels(10)
                .minConfidence(70F) // 원하는 신뢰도 수준
                .build();

        DetectLabelsResponse response = rekognitionClient.detectLabels(request);
        return response.labels();
    }

    /**
     * 라벨 리스트에 금지 키워드가 포함되어 있는지 검사
     */
    public boolean containsForbiddenLabel(List<Label> labels) {
        return labels.stream()
                .map(label -> label.name().toLowerCase())
                .anyMatch(FORBIDDEN_LABELS::contains);
    }
    public boolean containsCatLabel(List<Label> labels) {
        return labels.stream()
                .map(label -> label.name().toLowerCase())
                .anyMatch(CAT_LABELS::contains);
    }
}