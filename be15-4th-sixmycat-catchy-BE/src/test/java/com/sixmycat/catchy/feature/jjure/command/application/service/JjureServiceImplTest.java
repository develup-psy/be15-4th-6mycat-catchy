package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUploadRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureRepository;
import com.sixmycat.catchy.feature.jjure.command.domain.service.MemberValidationService;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JjureServiceImplTest {

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

    @Test
    @DisplayName("성공 - 올바른 요청 시 Jjure 저장 성공")
    void givenValidRequest_whenUploadJjure_thenSavesSuccessfully() {
        // given
        JjureUploadRequest request = new JjureUploadRequest("설명", "uploads/video.mp4", "uploads/thumbnail.jpg");

        // when
        jjureService.uploadJjure(request, 1L);

        // then
        verify(memberValidationService).validateUploadable(1L);
        verify(jjureRepository).save(any(Jjure.class));
    }

    @Test
    @DisplayName("유효성 실패 - fileKey가 null이면 실패")
    void givenNullFileKey_whenValidate_thenFails() {
        JjureUploadRequest request = new JjureUploadRequest("설명", null, "uploads/thumb.jpg");
        Set<ConstraintViolation<JjureUploadRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fileKey")));
    }

    @Test
    @DisplayName("유효성 실패 - fileKey가 빈 문자열이면 실패")
    void givenEmptyFileKey_whenValidate_thenFails() {
        JjureUploadRequest request = new JjureUploadRequest("설명", "", "uploads/thumb.jpg");
        Set<ConstraintViolation<JjureUploadRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fileKey")));
    }

    @Test
    @DisplayName("유효성 실패 - thumbnail_url이 null이면 실패")
    void givenNullThumbnailUrl_whenValidate_thenFails() {
        JjureUploadRequest request = new JjureUploadRequest("설명", "uploads/video.mp4", null);
        Set<ConstraintViolation<JjureUploadRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("thumbnail_url")));
    }

    @Test
    @DisplayName("저장 필드 검증 - 저장된 Jjure의 모든 필드가 정확히 설정되어야 함")
    void givenValidRequest_whenUploadJjure_thenFieldsShouldBeCorrect() {
        // given
        String caption = "쭈르 설명입니다";
        String fileKey = "uploads/video.mp4";
        String thumbnailUrl = "uploads/thumb.jpg";
        JjureUploadRequest request = new JjureUploadRequest(caption, fileKey, thumbnailUrl);
        ArgumentCaptor<Jjure> captor = ArgumentCaptor.forClass(Jjure.class);

        // when
        jjureService.uploadJjure(request, 1L);

        // then
        verify(jjureRepository).save(captor.capture());
        Jjure saved = captor.getValue();

        assertEquals(caption, saved.getCaption());
        assertEquals(fileKey, saved.getFileKey());
        assertEquals(thumbnailUrl, saved.getThumbnailUrl());
        assertNull(saved.getMusicUrl());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertNull(saved.getDeletedAt());
    }

    @Test
    @DisplayName("검증 실패 - 유효하지 않은 요청 시 저장 로직 호출되지 않아야 함")
    void givenInvalidRequest_whenUploadJjure_thenRepositoryNotCalled() {
        JjureUploadRequest request = new JjureUploadRequest(null, null, null);
        Set<ConstraintViolation<JjureUploadRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        verify(jjureRepository, never()).save(any());
    }
}
