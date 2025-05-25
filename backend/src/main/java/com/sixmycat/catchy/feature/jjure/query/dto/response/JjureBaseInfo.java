package com.sixmycat.catchy.feature.jjure.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JjureBaseInfo {
    private Long id;
    private Long authorId;
    private String nickname;
    private String profileImageUrl;
    private String fileKey;
    private String caption;
    private String musicUrl;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private String thumbnailUrl;
}