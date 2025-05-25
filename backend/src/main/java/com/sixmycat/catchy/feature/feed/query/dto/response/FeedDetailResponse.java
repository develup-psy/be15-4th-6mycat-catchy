package com.sixmycat.catchy.feature.feed.query.dto.response;

import com.sixmycat.catchy.common.dto.AuthorInfo;
import com.sixmycat.catchy.common.dto.CommentPreview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FeedDetailResponse {

    private Long id;

    private AuthorInfo author;

    private List<String> imageUrls;

    private String content;

    private String musicUrl;

    private int likeCount;

    private int commentCount;

    private CommentPreview commentPreview;

    private boolean isLiked;

    private boolean isMine;

    private LocalDateTime createdAt;

}