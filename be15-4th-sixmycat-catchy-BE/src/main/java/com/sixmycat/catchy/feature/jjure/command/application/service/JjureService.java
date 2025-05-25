package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUpdateRequest;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUploadRequest;
import org.springframework.web.multipart.MultipartFile;

public interface JjureService {
    void uploadJjure(JjureUploadRequest request, Long memberId);

    void updateJjure(JjureUpdateRequest request, Long memberId, Long jjureId);

    String uploadThumbnailImage(MultipartFile file);

    void deleteJjure(Long memberId, Long jjureId);
}

