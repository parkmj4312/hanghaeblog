package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentResponseDto {

    private String username;
    private String contents;
    private Long userId;
    private Long boardId;
    private List<Comment> commentList;

    public CommentResponseDto(String username, String contents, Long userId, Long boardId) {
        this.username = username;
        this.contents = contents;
        this.userId = userId;
        this.boardId = boardId;
    }
    public CommentResponseDto(Comment comment) {
        this.username = comment.getUsername();
        this.contents = comment.getContents();
        this.userId = comment.getUserId();
        this.boardId = comment.getBoardId();
    }
}
