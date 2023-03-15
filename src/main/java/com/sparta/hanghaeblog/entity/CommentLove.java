package com.sparta.hanghaeblog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommentLove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID",nullable = false)
    private Comment comment;
    @Column
    private boolean isLove = false;

    public CommentLove(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public void update(){
        if(this.isLove == false){
            this.isLove = true;
        }else{
            this.isLove = false;
        }
    }
}