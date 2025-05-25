package com.sixmycat.catchy.feature.jjure.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.s3.dto.PresignedUrlResponse;
import com.sixmycat.catchy.common.s3.service.S3PresignedUrlService;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUpdateRequest;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUploadRequest;
import com.sixmycat.catchy.feature.jjure.command.application.service.JjureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jjure/upload")
@Tag(name = "쭈르", description = "쭈르 업로드 API")
@Slf4j
public class JjureCommandController {
    private final S3PresignedUrlService presignedUrlService;
    private final JjureService jjureService;

    /* Presigned URL 생성 핸들러 */
    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUrl(@RequestParam String filename, @RequestParam String contentType) {
        PresignedUrlResponse response = presignedUrlService.generatePresignedUploadUrl(filename, contentType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* S3에 등록된 영상에 대한 메타데이터 저장 핸들러 */
    @Operation(summary = "쭈르 업로드", description = "S3에 업로드된 쭈르 메타데이터를 저장합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> uploadJjure(
            @Valid @RequestBody JjureUploadRequest request,
            @AuthenticationPrincipal String memberId
    ) {
        log.info("userId: {}", memberId);
        jjureService.uploadJjure(request, Long.parseLong(memberId));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* S3에 썸네일 저장 */
    @PostMapping("/thumbnail")
    public ApiResponse<String> uploadThumbnailImage(@RequestPart MultipartFile file) {
        String url = jjureService.uploadThumbnailImage(file);
        return ApiResponse.success(url);
    }

    /* 쭈르 수정 핸들러 */
    @Operation(summary = "쭈르 수정", description = "캡션과 파일 키(fileKey)를 수정합니다.")
    @PutMapping("/{jjureId}")
    public ResponseEntity<ApiResponse<Void>> updateJjure(
            @PathVariable Long jjureId,
            @RequestBody @Valid JjureUpdateRequest request,
            @AuthenticationPrincipal String memberId
    ) {
        jjureService.updateJjure(request, Long.parseLong(memberId), jjureId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 쭈르 삭제*/
    @DeleteMapping("/{jjureId}")
    public ApiResponse<Void> deleteFeed(@PathVariable Long jjureId,
                                        @AuthenticationPrincipal String memberId) {
        jjureService.deleteJjure(Long.parseLong(memberId), jjureId);
        return ApiResponse.success(null);
    }


}
