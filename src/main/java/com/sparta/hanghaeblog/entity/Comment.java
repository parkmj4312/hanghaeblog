package com.sparta.hanghaeblog.entity;

import com.sparta.hanghaeblog.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "BOARD_ID",nullable = false)
    private Board board;

    public Comment(CommentRequestDto requestDto, User user, Board board) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.user = user;
        this.board = board;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.username = commentRequestDto.getUsername();
        this.contents = commentRequestDto.getContents();
    }
}
