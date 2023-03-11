package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardRequestDto {
    private String title;
    private String username;
    private String contents;
    private List<Comment> commentList;
}