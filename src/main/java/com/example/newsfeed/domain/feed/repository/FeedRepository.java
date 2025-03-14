package com.example.newsfeed.domain.feed.repository;

import com.example.newsfeed.domain.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Feed> findAllByOrderByUpdatedAtDesc();

    @Query("SELECT f FROM Feed f WHERE f.createdAt BETWEEN :startDate AND :endDate ORDER BY f.createdAt DESC")
    List<Feed> findFeedsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
