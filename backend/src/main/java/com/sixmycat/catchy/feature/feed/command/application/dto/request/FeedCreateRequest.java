package com.sixmycat.catchy.feature.feed.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedCreateRequest {
    private String content;
    private List<String> imageUrls;
    private String musicUrl;
}