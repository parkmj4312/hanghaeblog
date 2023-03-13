package com.sparta.hanghaeblog.repository;

import com.sparta.hanghaeblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardIdOrderByCreatedAtAsc(Long boardId);
    Optional<Comment> findByBoardId(Long boardId);

    void deleteAllByBoardId(Long boardId);
}
