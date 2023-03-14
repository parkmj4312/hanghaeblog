package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.id = board.getId();
    }
    public BoardResponseDto(Board board, List<CommentResponseDto> commentList) {
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.id = board.getId();
        this.createdAt = board.getCreatedAt();
        this.commentList = commentList;
    }
}
