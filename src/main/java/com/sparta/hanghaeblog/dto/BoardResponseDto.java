package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.entity.Comment;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private Long userId;
    private List<Comment> commentList;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.userId = board.getUserId();
        this.id = board.getId();
    }
    public BoardResponseDto(Board board, List<Comment> commentList) {
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.userId = board.getUserId();
        this.id = board.getId();;
        this.commentList = commentList;
    }
}
