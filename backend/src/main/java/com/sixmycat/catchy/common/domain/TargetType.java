package com.sixmycat.catchy.common.domain;

import lombok.Getter;

@Getter
public enum TargetType {
    JJURE("쭈르"),
    FEED("피드");

    private final String name;
    TargetType(String name) { this.name = name; }
}
