package com.example.newsfeed.domain.feed.repository;

import com.example.newsfeed.domain.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
