package com.sixmycat.catchy.feature.feed.command.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedImageService {

    String uploadSingleImage(MultipartFile file);

    List<String> uploadMultipleImages(List<MultipartFile> files);
}