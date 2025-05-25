package com.sixmycat.catchy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class AuthorInfo {
    private Long authorId;
    private String nickname;
    private String profileImageUrl;
}