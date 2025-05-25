package com.sixmycat.catchy.feature.feed.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.feed.command.application.service.FeedImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed")
public class FeedImageController {

    private final FeedImageService feedImageService;

    @PostMapping("/image")
    public ApiResponse<String> uploadFeedImage(@RequestPart MultipartFile file) {
        String url = feedImageService.uploadSingleImage(file);
        return ApiResponse.success(url);
    }

    @PostMapping("/images")
    public ApiResponse<List<String>> uploadFeedImages(@RequestPart List<MultipartFile> files) {
        List<String> urls = feedImageService.uploadMultipleImages(files);
        return ApiResponse.success(urls);
    }
}