package com.sixmycat.catchy.feature.feed.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FeedBaseInfo {
    private Long id;
    private Long authorId;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private String musicUrl;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
}