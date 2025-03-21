package com.example.newsfeed.domain.feed.service;

import com.example.newsfeed.domain.feed.dto.request.FeedRequestDto;
import com.example.newsfeed.domain.feed.dto.response.FeedResponseDto;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional
    public FeedResponseDto createFeed(Long userId, FeedRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
        );
        Feed feed = Feed.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
        feedRepository.save(feed);
        return new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId());
    }

    @Transactional(readOnly = true)
    public FeedResponseDto getFeedById(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다.")
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
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다.")
        );
        if (!feed.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "글 수정은 작성자만 가능합니다.");
        }
        feed.updateFeed(dto.getTitle(), dto.getContent());
        return  new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId());
    }

    @Transactional
    public void deleteFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다.")
        );
        if (!feed.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "글 삭제는 작성자만 가능합니다.");
        }
        feedRepository.delete(feed);
    }

    @Transactional(readOnly = true)
    public List<FeedResponseDto> getFeedsSortedByUpdatedAt() {
        return feedRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(feed -> new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FeedResponseDto> getFeedsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return feedRepository.findFeedsByCreatedAtBetween(startDate, endDate).stream()
                .map(feed -> new FeedResponseDto(feed.getId(), feed.getTitle(), feed.getContent(), feed.getUserId()))
                .collect(Collectors.toList());
    }
}
