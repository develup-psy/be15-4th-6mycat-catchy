package com.sixmycat.catchy.feature.jjure.query.dto.response;

import com.sixmycat.catchy.common.dto.AuthorInfo;
import com.sixmycat.catchy.common.dto.CommentPreview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class JjureDetailResponse {
    private Long id;

    private AuthorInfo author;

    private String fileKey;

    private String caption;

    private String musicUrl;

    private int likeCount;

    private int commentCount;

    private CommentPreview commentPreview;

    private boolean isLiked;

    private boolean isMine;

    private LocalDateTime createdAt;

    private String thumbnailUrl;
}