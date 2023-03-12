package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.repository.BoardRepository;
import com.sparta.hanghaeblog.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Board board = boardRepository.saveAndFlush(new Board(requestDto, user));

            return new BoardResponseDto(board);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards()
    {
        List<Board> boardLisgt = boardRepository.findAllByOrderByCreatedAtAsc();
        List<BoardResponseDto> responseDtos = new ArrayList<>();

        for(Board board : boardLisgt){
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
    public Board getBoard(long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            if(board.getUser().getId() == user.getId()){
                board.update(requestDto);
            }
            return new BoardResponseDto(board);
        } else {
            return null;
        }

    }

    @Transactional
    public String deleteBoard(Long id,BoardRequestDto requestDto, HttpServletRequest request) {

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            if(board.getUser().getId() == user.getId()){
                boardRepository.deleteById(id);
                return "게시글을 삭제하였습니다.";
            }else{
                return "게시글 삭제에 실패하였습니다.";
            }

        } else {
            return "토큰이 없습니다.";
        }
    }
}
