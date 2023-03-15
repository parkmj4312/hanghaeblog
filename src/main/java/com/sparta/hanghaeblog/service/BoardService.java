package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.entity.*;
import com.sparta.hanghaeblog.repository.BoardLoveRepository;
import com.sparta.hanghaeblog.repository.BoardRepository;
import com.sparta.hanghaeblog.repository.CommentRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardLoveRepository boardLoveRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Board board = boardRepository.saveAndFlush(new Board(requestDto, user));

        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> responseDtos = new ArrayList<>();

        for (Board board : boardList) {
            List<CommentResponseDto> commentResponseList = getCommentResponseList(board);
            Collections.reverse(commentResponseList);
            responseDtos.add(new BoardResponseDto(board, commentResponseList));
        }

        return responseDtos;
    }

    private List<CommentResponseDto> getCommentResponseList(Board board) {
        List<CommentResponseDto> replyResponseList = new ArrayList<>();
        for (Comment comment : board.getCommentList()) {
            replyResponseList.add(new CommentResponseDto(comment));
        }
        return replyResponseList;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        BoardResponseDto responseDto = new BoardResponseDto(board, getCommentResponseList(board));
        return responseDto;
    }

    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> update(Long id, BoardRequestDto requestDto, User user) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        if (board.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN) {
            board.update(requestDto);
            return new ResponseEntity("게시글을 수정 했습니다.", HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> deleteBoard(Long id, User user) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        if (board.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN) {
            commentRepository.deleteAllByBoardId(board.getId());
            boardRepository.deleteById(id);
            return new ResponseEntity("게시글을 삭제 했습니다.", HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

    }

    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> loveOk(Long id, User user) {

        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        List<BoardLove> boardLoveList = user1.getBoarLoveList();

        if (user != null) {

            for (BoardLove boardLoves : boardLoveList) {
                if (boardLoves.getBoard().getId() == board.getId() && boardLoves.getUser().getId() == user1.getId()) {
                    if (boardLoves.isLove() == false) {
                        boardLoves.update();
                        board.LoveOk();
                        return new ResponseEntity("게시글을 좋아요 했습니다.", HttpStatus.OK);
                    } else {
                        boardLoves.update();
                        board.LoveCancel();
                        return new ResponseEntity("좋아요를 취소 했습니다.", HttpStatus.OK);
                    }
                } else {
                    BoardLove boardLove = new BoardLove(board, user1);
                    boardLoveRepository.save(boardLove);
                    boardLove.update();
                    board.LoveOk();
                    return new ResponseEntity("게시글을 좋아요 했습니다.", HttpStatus.OK);
                }
            }
            if(boardLoveList.size() == 0){
                BoardLove boardLove = new BoardLove(board, user1);
                boardLoveRepository.save(boardLove);
                boardLove.update();
                board.LoveOk();
                return new ResponseEntity("게시글을 좋아요 했습니다.", HttpStatus.OK);
            }

        } else {
            throw new IllegalArgumentException("로그인 유저만 좋아요할 수 있습니다.");
        }
        return null;
    }
}
