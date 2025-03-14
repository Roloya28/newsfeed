package com.example.newsfeed.domain.feed.service;

import com.example.newsfeed.domain.feed.dto.request.FeedRequestDto;
import com.example.newsfeed.domain.feed.dto.response.FeedResponseDto;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional
    public FeedResponseDto createFeed(Long userId, FeedRequestDto dto) {
        Feed feed = Feed.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .userId(userId)
                .build();
        feedRepository.save(feed);
        return new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId());
    }

    @Transactional(readOnly = true)
    public FeedResponseDto getFeedById(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RuntimeException("피드를 찾을 수 없습니다.")
        );
        return new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId());
    }

    @Transactional(readOnly = true)
    public List<FeedResponseDto> getAllFeeds(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Feed> feeds = feedRepository.findAllByOrderByCreatedAtDesc(pageable);
        return feeds.map(feed -> new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId())).toList();
    }

    @Transactional
    public FeedResponseDto updateFeed(Long feedId, Long userId, FeedRequestDto dto) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RuntimeException("피드를 찾을 수 없습니다.")
        );
        if (!feed.getUserId().equals(userId)) {
            throw new RuntimeException("글 수정은 작성자만 가능합니다.");
        }
        feed.updateFeed(dto.getTitle(), dto.getContent());
        return  new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId());
    }

    @Transactional
    public void deleteFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RuntimeException("피드를 찾을 수 없습니다.")
        );
        if (!feed.getUserId().equals(userId)) {
            throw new RuntimeException("글 삭제는 작성자만 가능합니다.");
        }
        feedRepository.delete(feed);
    }
}
