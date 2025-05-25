package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.common.s3.S3Uploader;
import com.sixmycat.catchy.common.s3.dto.S3UploadResult;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUpdateRequest;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUploadRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureRepository;
import com.sixmycat.catchy.feature.jjure.command.domain.service.MemberValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class JjureServiceImpl implements JjureService {

    private final JjureRepository jjureRepository;
    private final MemberValidationService memberValidationService;
    private final S3Uploader s3Uploader;

    @Override
    public void uploadJjure(JjureUploadRequest request, Long memberId) {
        // member 탈퇴여부 확인
        memberValidationService.validateUploadable(memberId);

        LocalDateTime now = LocalDateTime.now();

        Jjure jjure = Jjure.builder()
                .memberId(memberId)
                .caption(request.getCaption())
                .fileKey(request.getFileKey())
                .musicUrl(null) // 음악 설정 연결 전까지 일단 Null로 처리
                .thumbnailUrl(request.getThumbnail_url())
                .createdAt(now)
                .updatedAt(now)
                .deletedAt(null)
                .build();

        try {
            jjureRepository.save(jjure);
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorCode.JJURE_UPLOAD_FAILED);
        }
    }

    @Override
    public void updateJjure(JjureUpdateRequest request, Long memberId, Long jjureId) {
        memberValidationService.validateUploadable(memberId);

        Jjure jjure = jjureRepository.findById(jjureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JJURE_NOT_FOUND));

        memberValidationService.validateJjureOwner(
                memberId,
                jjure.getMemberId(),
                ErrorCode.NO_PERMISSION_TO_UPDATE_JJURE
        );

        jjure.updateIfNotNull(request.getCaption(), request.getFileKey(), request.getThumbnailUrl());
    }


    @Override
    public String uploadThumbnailImage(MultipartFile file) {
        S3UploadResult result = s3Uploader.uploadFile(file, "uploads");

        return result.url();
    }

    @Override
    public void deleteJjure(Long memberId, Long jjureId) {
        Jjure jjure = jjureRepository.findById(jjureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JJURE_NOT_FOUND));

        memberValidationService.validateJjureOwner(memberId, jjure.getMemberId(), ErrorCode.NO_PERMISSION_TO_DELETE_JJURE);

//        s3Uploader.deleteFile(); 파일키로 삭제를 해줘야 되는데...
        jjure.markAsDeleted();
    }

}

