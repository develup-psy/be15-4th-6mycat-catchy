package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedUpdateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedRepository;
import com.sixmycat.catchy.feature.feed.command.domain.service.FeedDomainService;
import com.sixmycat.catchy.feature.rekognition.command.application.service.RekognitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedCommandServiceImpl implements FeedCommandService {

    private final FeedDomainService feedDomainService;
    private final FeedRepository feedRepository;
    private final RekognitionService rekognitionService;

    @Override
    @Transactional
    public Long createFeed(FeedCreateRequest request, Long memberId) {
        // 1. 유효성 검증(비즈니스 로직)
        feedDomainService.validateContentLength(request.getContent());
        feedDomainService.validateImageCount(request.getImageUrls());
        boolean hasCatImage = true;

        List<String> imageKeys = request.getImageUrls().stream()
                .map(this::extractKeyFromUrl)  // CloudFront URL에서 key 추출
                .toList();


        for (String key : imageKeys) {
            log.info("Detected key: {}", key);
            List<Label> labels = rekognitionService.detectLabels(key);

            if (rekognitionService.containsForbiddenLabel(labels)) {
                throw new BusinessException(ErrorCode.FORBIDDEN_LABEL_FOUND);
            }

            if (!rekognitionService.containsCatLabel(labels)) {
                hasCatImage = false;
            }
        }

        if (!hasCatImage) {
            throw new BusinessException(ErrorCode.NO_CAT_LABEL_FOUND);
        }

        // 2. Feed 객체 생성
        Feed feed = Feed.create(request.getContent(), memberId, request.getMusicUrl(), request.getImageUrls());

        // 3. 저장 후 feedId 반환
        return feedRepository.save(feed).getId();
    }

    @Override
    @Transactional
    public void updateFeed(Long feedId, FeedUpdateRequest request, Long memberId) {
        Feed feed =  feedRepository.findById(feedId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEED_NOT_FOUND));

        feedDomainService.validateFeedOwner(feed, memberId);
        feedDomainService.validateContentLength(request.getContent());
        feedDomainService.validateImageCount(request.getImageUrls());

        // 1. Feed 기본 정보 수정
        feed.updateContent(request.getContent());
        feed.updateMusicUrl(request.getMusicUrl());

        // 2. 이미지 전체 교체 (삭제 후 재등록)
        feed.clearImages();
        List<String> newUrls = request.getImageUrls();
        for (int i = 0; i < newUrls.size(); i++) {
            feed.addImage(newUrls.get(i), i); // 순서도 저장
        }
    }

    @Override
    @Transactional
    public void deleteFeed(Long feedId, Long memberId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEED_NOT_FOUND));

        feedDomainService.validateFeedOwner(feed, memberId);
        feed.markAsDeleted();

    }

    private String extractKeyFromUrl(String url) {
        return url.substring(url.indexOf("/feed/") + 1); // 혹은 정규식으로 파싱
    }
}