package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.User;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String username;
    private String contents;
    private Long userId;
    private Long boardId;

    public CommentResponseDto(String username, String contents, User user, Board board) {
        this.username = username;
        this.contents = contents;
        this.userId = user.getId();
        this.boardId = board.getId();
    }
    public CommentResponseDto(Comment comment) {
        this.username = comment.getUsername();
        this.contents = comment.getContents();
        this.userId = comment.getUser().getId();
        this.boardId = comment.getBoard().getId();
    }
}
