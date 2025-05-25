package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.common.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedImageServiceImpl implements FeedImageService {

    private final S3Uploader s3Uploader;

    @Override
    public String uploadSingleImage(MultipartFile file) {
        return s3Uploader.uploadFile(file, "feed").url();
    }

    @Override
    public List<String> uploadMultipleImages(List<MultipartFile> files) {
        return s3Uploader.uploadFiles(files, "feed");
    }
}