package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.CommentRequestDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.entity.*;
import com.sparta.hanghaeblog.repository.BoardRepository;
import com.sparta.hanghaeblog.repository.CommentLoveRepository;
import com.sparta.hanghaeblog.repository.CommentRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentLoveRepository commentLoveRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, Long id, User user) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, user, board));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> update(Long id, CommentRequestDto requestDto, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () ->  new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        if (comment.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN) {
            comment.update(requestDto);
            return new ResponseEntity("댓글을 수정 했습니다.", HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }


    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> deleteComment(Long id, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if (comment.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN) {
            commentRepository.deleteById(id);
            return new ResponseEntity("댓글을 삭제 했습니다.", HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> loveOk(Long id, User user) {

        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        List<CommentLove> commentLoveList = user1.getCommentLoveList();

        if (user != null) {

            for (CommentLove commentLoves : commentLoveList) {
                if (commentLoves.getComment().getId() == comment.getId() && commentLoves.getUser().getId() == user1.getId()) {
                    if (commentLoves.isLove() == false) {
                        commentLoves.update();
                        comment.LoveOk();
                        return new ResponseEntity("댓글을 좋아요 했습니다.", HttpStatus.OK);
                    } else {
                        commentLoves.update();
                        comment.LoveCancel();
                        return new ResponseEntity("좋아요를 취소 했습니다.", HttpStatus.OK);
                    }
                }
            }
                CommentLove commentLove = new CommentLove(comment, user1);
                commentLoveRepository.save(commentLove);
                commentLove.update();
                comment.LoveOk();
                return new ResponseEntity("댓글을 좋아요 했습니다.", HttpStatus.OK);

        } else {
            throw new IllegalArgumentException("로그인 유저만 좋아요할 수 있습니다.");
        }
    }
}

