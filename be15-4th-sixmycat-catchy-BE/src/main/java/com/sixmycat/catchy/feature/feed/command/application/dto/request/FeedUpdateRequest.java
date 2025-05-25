package com.sixmycat.catchy.feature.feed.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedUpdateRequest {
    private String content;
    private String musicUrl;
    private List<String> imageUrls;
}