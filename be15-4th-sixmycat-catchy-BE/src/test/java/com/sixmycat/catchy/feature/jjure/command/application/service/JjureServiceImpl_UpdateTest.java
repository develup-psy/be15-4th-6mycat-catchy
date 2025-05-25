package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.common.s3.S3Uploader;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUpdateRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureRepository;
import com.sixmycat.catchy.feature.jjure.command.domain.service.MemberValidationService;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class JjureServiceImpl_UpdateTest {

    @InjectMocks
    private JjureServiceImpl jjureService;

    @Mock
    private JjureRepository jjureRepository;

    @Mock
    private MemberValidationService memberValidationService;

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

//    @Test
//    @DisplayName("성공 - 쭈르 수정")
//    void givenValidUpdateRequest_whenUpdateJjure_thenUpdateSuccess() {
//        // given
//        Long memberId = 1L;
//        Long jjureId = 100L;
//        Jjure jjure = mock(Jjure.class);
//
//        when(jjureRepository.findById(jjureId)).thenReturn(Optional.of(jjure));
//
//        JjureUpdateRequest request = new JjureUpdateRequest("업데이트 설명", "newKey.mp4", "thumbnail.jpg");
//
//        // when
//        jjureService.updateJjure(request, memberId, jjureId);
//
//        // then
//        verify(memberValidationService).validateUploadable(memberId);
//        verify(memberValidationService).validateJjureOwner(memberId, jjure.getMemberId(), ErrorCode.NO_PERMISSION_TO_UPDATE_JJURE);
//        verify(jjure).update("업데이트 설명", "newKey.mp4", "thumbnail.jpg");
//    }

    @Test
    @DisplayName("실패 - 수정 시 쭈르 존재하지 않음")
    void givenNonExistingJjure_whenUpdateJjure_thenThrowsException() {
        Long memberId = 1L;
        Long jjureId = 100L;
        when(jjureRepository.findById(jjureId)).thenReturn(Optional.empty());

        JjureUpdateRequest request = new JjureUpdateRequest("업데이트 설명", "fileKey", "thumb.jpg");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> jjureService.updateJjure(request, memberId, jjureId)
        );

        assertEquals(ErrorCode.JJURE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("성공 - 쭈르 삭제 성공")
    void givenValidRequest_whenDeleteJjure_thenMarkAsDeletedCalled() {
        Long memberId = 1L;
        Long jjureId = 99L;

        Jjure jjure = mock(Jjure.class);
        when(jjureRepository.findById(jjureId)).thenReturn(Optional.of(jjure));
        when(jjure.getMemberId()).thenReturn(memberId);

        // when
        jjureService.deleteJjure(memberId, jjureId);

        // then
        verify(jjure).markAsDeleted();
    }

//    @Test
//    @DisplayName("실패 - 쭈르 삭제 시 대상 없음")
//    void givenInvalidJjureId_whenDeleteFeed_thenThrows() {
//        Long memberId = 1L;
//        Long jjureId = 999L;
//        when(jjureRepository.findById(jjureId)).thenReturn(Optional.empty());
//
//        BusinessException ex = assertThrows(BusinessException.class, () -> {
//            jjureService.deleteJjure(memberId, jjureId);
//        });
//
//        assertEquals(ErrorCode.FEED_NOT_FOUND, ex.getErrorCode());
//    }

    @Test
    @DisplayName("예외 - 본인이 아닌 다른 회원이 수정하면 NO_PERMISSION_TO_UPDATE_JJURE 예외 발생")
    void givenWrongMemberId_whenUpdate_thenThrowsUnauthorized() {
        // given
        Long ownerId = 1L;
        Long otherMemberId = 99L;
        Long jjureId = 100L;

        Jjure jjure = Jjure.builder()
                .id(jjureId)
                .memberId(ownerId)
                .caption("이전 설명")
                .fileKey("file.mp4")
                .build();

        when(jjureRepository.findById(jjureId)).thenReturn(Optional.of(jjure));

        JjureUpdateRequest request = new JjureUpdateRequest("수정된 설명", "new.mp4", "thumbnail-file.png");

        // 👇 예외 발생 설정
        doThrow(new BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE_JJURE))
                .when(memberValidationService)
                .validateJjureOwner(otherMemberId, ownerId, ErrorCode.NO_PERMISSION_TO_UPDATE_JJURE);

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            jjureService.updateJjure(request, otherMemberId, jjureId);
        });

        assertEquals(ErrorCode.NO_PERMISSION_TO_UPDATE_JJURE, ex.getErrorCode());
    }


//    @Test
//    @DisplayName("유효성 실패 - fileKey가 빈 문자열이면 실패")
//    void givenEmptyFileKey_whenValidate_thenFails() {
//        JjureUpdateRequest request = new JjureUpdateRequest("설명", "", "");
//        Set<ConstraintViolation<JjureUpdateRequest>> violations = validator.validate(request);
//
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fileKey")));
//    }
}
