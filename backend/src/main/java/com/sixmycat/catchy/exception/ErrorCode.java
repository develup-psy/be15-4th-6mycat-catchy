package com.sixmycat.catchy.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {

    VALIDATION_ERROR("01000", "입력 값 검증 오류입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("01001", "내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_USER("01002", "인증되지 않은 사용자입니다.",HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT("01003", "JWT 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT("01004", "잘못된 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT("01005", "지원하지 않는 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_JWT("01006", "JWT 클레임이 비어있습니다.", HttpStatus.UNAUTHORIZED),
    SOCIAL_PLATFORM_NOT_SUPPORTED("01007", "지원하지 않는 소셜 플랫폼입니다.", HttpStatus.BAD_REQUEST),
    TEMP_MEMBER_NOT_FOUND("01008", "임시 회원 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    USING_NICKNAME("01009", "이미 사용중인 닉네임입니다.", HttpStatus.BAD_REQUEST),
    INVALID_NICKNAME_FORMAT("01010", "닉네임 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_OR_BLANK_NICKNAME("01011", "닉네임은 공백이거나 비워둘 수 없습니다.", HttpStatus.BAD_REQUEST),
    WRONG_NICKNAME_LENGTH("01012", "닉네임 길이는 3~30자입니다.", HttpStatus.BAD_REQUEST),

    // 멤버
    MEMBER_NOT_FOUND("01013", "회원을 찾을 수 업습니다", HttpStatus.NOT_FOUND),
    MEMBER_ALREADY_DELETED("01014", "탈퇴한 회원입니다", HttpStatus.CONFLICT),
    MEMBER_PROFILE_IMAGE_NOT_FOUND("01015", "프로필 이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 피드
    FEED_NOT_FOUND("04000", "피드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    FEED_CONTENT_TOO_LONG("04001", "피드 내용은 500자 이하여야 합니다.", HttpStatus.BAD_REQUEST),
    FEED_IMAGE_REQUIRED("04002", "피드에는 최소 1개의 이미지가 필요합니다.", HttpStatus.BAD_REQUEST),
    FEED_IMAGE_TOO_MANY("04003", "피드에는 최대 5개의 이미지만 등록할 수 있습니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN_LABEL_FOUND("04004", "강아지가 발견되었습니다.", HttpStatus.BAD_REQUEST),
    NO_CAT_LABEL_FOUND("04005", "피드에 고양이가 아닌 이미지가 포함되었습니다.", HttpStatus.BAD_REQUEST),

    //차단
    CANNOT_BLOCK_SELF("03001", "자기 자신은 차단할 수 없습니다.",HttpStatus.BAD_REQUEST),
    ALREADY_BLOCKED("03002", "이미 차단한 사용자입니다.",HttpStatus.BAD_REQUEST),
    BLOCK_NOT_FOUND("03003", "차단 내역이 없습니다.",HttpStatus.BAD_REQUEST),

    // 쮸르
    JJURE_NOT_FOUND("05000","쭈르를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    JJURE_UPLOAD_FAILED("05001","쭈르를 등록에 실패했습니다", HttpStatus.BAD_REQUEST),
    NO_PERMISSION_TO_UPDATE_JJURE("05002", "수정 권한이 없습니다",HttpStatus.FORBIDDEN),
    INVALID_FILE_TYPE("05003","유효하지 않은 파일 타입입니다." ,HttpStatus.BAD_REQUEST),
    NO_PERMISSION_TO_DELETE_JJURE("05004", "삭제 권한이 없습니다",HttpStatus.FORBIDDEN),

    //댓글
    COMMENT_NOT_FOUND("11000", "해당 상위댓글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PARENT_COMMENT("11001", "부모 댓글의 targetType이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    //게임
    GAME_SCORE_NOT_FOUND("06000","회원에 대한 게임 점수를 조회할 수 없습니다",HttpStatus.NOT_FOUND),

    //프로필
    CAT_NOT_FOUND("10000", "고양이를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    //좋아요
    ALREADY_LIKED("09000", "이미 좋아요한 항목입니다.", HttpStatus.BAD_REQUEST),
    LIKE_NOT_FOUND("09001", "좋아요 하지 않은 항목입니다.", HttpStatus.BAD_REQUEST),

    //팔로우
    INVALID_FOLLOW("11000", "자기 자신은 팔로우할 수 없습니다", HttpStatus.BAD_REQUEST),
    ALREADY_FOLLOWED("11001","이미 팔로우 중입니다" ,HttpStatus.BAD_REQUEST ),
    NOT_FOLLOWED("11002","팔로우 중이 아닙니다", HttpStatus.BAD_REQUEST ),
    FOLLOW_REQUEST_NOT_FOUND("11003", "요청한 팔로우가 없습니다", HttpStatus.BAD_REQUEST),
    ALREADY_REJECTED("11004","이미 언팔로우한 대상입니다", HttpStatus.BAD_REQUEST),;


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
