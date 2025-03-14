package com.example.newsfeed.domain.comment.repository;

import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByFeed(Feed feed);
}
