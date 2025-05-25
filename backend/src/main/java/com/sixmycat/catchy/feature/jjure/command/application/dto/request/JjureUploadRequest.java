package com.sixmycat.catchy.feature.jjure.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "쭈르 업로드 요청 DTO")
public class JjureUploadRequest {

    @Schema(description = "쭈르 캡션", example = "우리 고양이 쭈르 영상")
    private String caption;

    @Schema(description = "S3에 저장된 영상에 접근할 수 있는 FileKey")
    @NotBlank
    private String fileKey;

    @Schema(description = "썸네일 이미지")
    @NotBlank
    private String thumbnail_url;
}