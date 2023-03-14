package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.User;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private String username;
    private String contents;
    private Long userId;
    private Long boardId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUsername();
        this.contents = comment.getContents();
        this.userId = comment.getUser().getId();
        this.boardId = comment.getBoard().getId();
        this.modifiedAt = comment.getModifiedAt();
        this.createdAt = comment.getCreatedAt();
    }
}
