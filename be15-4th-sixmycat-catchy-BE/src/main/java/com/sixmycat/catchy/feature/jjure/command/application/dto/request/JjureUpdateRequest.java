package com.sixmycat.catchy.feature.jjure.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JjureUpdateRequest {

    private String caption;

    private String fileKey;

    private String thumbnailUrl;
}
