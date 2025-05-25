package com.sixmycat.catchy.feature.feed.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String content;

    private Long memberId;

    private String musicUrl;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedImage> feedImages = new ArrayList<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    // 정적 팩토리 메서드
    public static Feed create(String content, Long memberId, String musicUrl, List<String> imageUrls) {
        Feed feed = new Feed();
        feed.content = content;
        feed.memberId = memberId;
        feed.musicUrl = musicUrl;

        for (int i = 0; i < imageUrls.size(); i++) {
            String imageUrl = imageUrls.get(i);
            feed.feedImages.add(FeedImage.create(feed, imageUrl, i)); // 순서 정보 추가
        }

        return feed;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public void clearImages() {
        this.feedImages.clear();
    }

    public void addImage(String imageUrl, int order) {
        this.feedImages.add(FeedImage.create(this, imageUrl, order));
    }
}