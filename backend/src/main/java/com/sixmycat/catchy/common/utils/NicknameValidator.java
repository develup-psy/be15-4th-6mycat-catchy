package com.sixmycat.catchy.common.utils;

import java.util.regex.Pattern;

public class NicknameValidator {

    private static final String NICKNAME_REGEX = "^[a-zA-Z0-9^._]+$";
    private static final Pattern PATTERN = Pattern.compile(NICKNAME_REGEX);

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 30;

    // 공백, 빈 문자열 검사
    public static boolean isEmptyOrBlank(String nickname) {
        return nickname == null || nickname.isBlank() || nickname.contains(" ");
    }

    // 길이 조건 검사
    public static boolean isLengthValid(String nickname) {
        return nickname.length() >= MIN_LENGTH && nickname.length() <= MAX_LENGTH;
    }

    // 정규식 패턴 검사
    public static boolean isPatternValid(String nickname) {
        return PATTERN.matcher(nickname).matches();
    }
}
