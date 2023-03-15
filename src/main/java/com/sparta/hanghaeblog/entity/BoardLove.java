package com.sparta.hanghaeblog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class BoardLove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID",nullable = false)
    private Board board;
    @Column
    private boolean isLove = false;

    public BoardLove(Board board, User user) {
        this.board = board;
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
