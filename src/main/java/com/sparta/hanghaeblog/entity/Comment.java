package com.sparta.hanghaeblog.entity;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
//    @JoinColumn(name = "USER_ID", nullable = false)
//    @ManyToOne
//    private User user;
//    @JoinColumn(name = "BOARD_ID", nullable = false)
//    @ManyToOne
//    private Board board;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long boardId;

    public Comment(CommentRequestDto requestDto, Long userId, Long boardId) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.userId = userId;
        this.boardId = boardId;
    }
}
