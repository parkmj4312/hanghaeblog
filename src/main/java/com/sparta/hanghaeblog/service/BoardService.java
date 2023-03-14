package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.repository.BoardRepository;
import com.sparta.hanghaeblog.repository.CommentRepository;
import com.sparta.hanghaeblog.repository.UserRepository;

import io.jsonwebtoken.Claims;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto,User user) {

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Board board = boardRepository.saveAndFlush(new Board(requestDto, user));

        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards()
    {
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtAsc();
        List<BoardResponseDto> responseDtos = new ArrayList<>();

        for(Board board : boardList){
            List<CommentResponseDto> commentResponseList = getCommentResponseList(board);
            responseDtos.add(new BoardResponseDto(board,commentResponseList));
        }

        return responseDtos;
    }
    private  List<CommentResponseDto> getCommentResponseList (Board board) {
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
        BoardResponseDto responseDto = new BoardResponseDto(board,getCommentResponseList(board));
        return responseDto;
    }

    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> update(Long id, BoardRequestDto requestDto, User user) {

            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            if(board.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN){
                board.update(requestDto);
                return new ResponseEntity("게시글을 수정 했습니다.",HttpStatus.OK);
            }else{
                return new ResponseEntity("작성자만 삭제/수정할 수 있습니다.",HttpStatus.BAD_REQUEST);
            }
    }

    @Transactional
    public ResponseEntity<Map<String, HttpStatus>> deleteBoard(Long id, User user) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        if (board.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN) {
            commentRepository.deleteAllByBoardId(board.getId());
            boardRepository.deleteById(id);
            return new ResponseEntity("게시글을 삭제 했습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }

    }
}
