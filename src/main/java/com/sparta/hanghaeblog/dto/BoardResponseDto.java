package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private String title;
    private String username;
    private String contents;
    private Long userId;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.userId = board.getUserId();
    }
}
