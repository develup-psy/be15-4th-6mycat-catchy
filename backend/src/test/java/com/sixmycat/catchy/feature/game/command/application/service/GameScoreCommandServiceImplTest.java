package com.sixmycat.catchy.feature.game.command.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sixmycat.catchy.feature.game.command.domain.aggregate.GameScore;
import com.sixmycat.catchy.feature.game.command.domain.repository.GameScoreRepository;
import com.sixmycat.catchy.feature.game.command.domain.service.GameDomainService;
import jakarta.persistence.EntityManager;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameScoreCommandServiceImplTest {
    @InjectMocks
    private GameCommandServiceImpl gameScoreService;

    @Mock
    private GameScoreRepository gameScoreRepository;

    @Mock
    private GameDomainService gameScoreDomainService;

    @Mock
    private EntityManager entityManager;

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

//    @Test
//    @DisplayName("성공 - 기존 점수보다 높을 경우 저장됨")
//    void givenBetterScore_whenSave_thenStored() {
//        // given
//        Long memberId = 1L;
//        Integer newScore = 40;
//        GameScore oldScore = GameScore.of(memberId, 50);
//
//        when(gameScoreRepository.findByMemberId(memberId)).thenReturn(Optional.of(oldScore));
//        when(gameScoreDomainService.shouldSaveNewScore(oldScore, newScore)).thenReturn(true);
//
//        // when
//        gameScoreService.saveIfBestScore(memberId, newScore);
//
//        // then
//        verify(gameScoreRepository, times(1)).save(any(GameScore.class));
//    }

    @Test
    @DisplayName("저장 안됨 - 기존 점수가 더 높거나 같을 경우 저장 안함")
    void givenWorseOrSameScore_whenSave_thenNotStored() {
        Long memberId = 1L;
        Integer newScore = 60;
        GameScore oldScore = GameScore.of(memberId, 50);

        when(gameScoreRepository.findByMemberId(memberId)).thenReturn(Optional.of(oldScore));
        when(gameScoreDomainService.shouldSaveNewScore(oldScore, newScore)).thenReturn(false);

        gameScoreService.saveIfBestScore(memberId, newScore);

        verify(gameScoreRepository, never()).save(any());
    }

    @Test
    @DisplayName("예외 - 저장 중 예외 발생 시 예외 전파 및 save 호출됨")
    void givenSaveFails_whenSave_thenExceptionThrown() {
        Long memberId = 1L;
        Integer newScore = 30;

        when(gameScoreRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
        when(gameScoreDomainService.shouldSaveNewScore(null, newScore)).thenReturn(true);
        doThrow(new RuntimeException("DB ERROR")).when(gameScoreRepository).save(any());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gameScoreService.saveIfBestScore(memberId, newScore)
        );

        assertEquals("DB ERROR", ex.getMessage());
        verify(gameScoreRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("상태 검증 - 저장된 게임 점수가 정확한 값으로 저장되어야 함")
    void givenValidScore_whenSave_thenFieldsCorrect() {
        Long memberId = 1L;
        Integer score = 42;
        ArgumentCaptor<GameScore> captor = ArgumentCaptor.forClass(GameScore.class);

        when(gameScoreRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
        when(gameScoreDomainService.shouldSaveNewScore(null, score)).thenReturn(true);

        gameScoreService.saveIfBestScore(memberId, score);

        verify(gameScoreRepository).save(captor.capture());
        GameScore saved = captor.getValue();

        assertEquals(score, saved.getScore());
        assertEquals(memberId, saved.getMemberId());
        assertNotNull(saved.getCreatedAt());
    }
}