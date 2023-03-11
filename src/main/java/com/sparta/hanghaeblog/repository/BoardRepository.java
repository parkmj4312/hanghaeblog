package com.sparta.hanghaeblog.repository;

import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedAtAsc();
}