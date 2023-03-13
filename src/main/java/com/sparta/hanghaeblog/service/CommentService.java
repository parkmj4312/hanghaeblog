package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.CommentRequestDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.BoardRepository;
import com.sparta.hanghaeblog.repository.CommentRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    public CommentResponseDto createComment(CommentRequestDto requestDto,Long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
            );
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Comment comment = commentRepository.saveAndFlush(new Comment(requestDto,user,board));

            return new CommentResponseDto(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다.");
        }
    }
    @Transactional(readOnly = true)
    public List<Comment> getComments(Long boardId) {
        return commentRepository.findAllByBoardIdOrderByCreatedAtAsc(boardId);
    }

    @Transactional
    public ResponseEntity<Map<String,HttpStatus>> update(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return new ResponseEntity("토큰이 유효하지 않습니다.",HttpStatus.BAD_REQUEST);
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            if(comment.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN){
                comment.update(requestDto);
                return new ResponseEntity("댓글을 수정 했습니다.",HttpStatus.OK);
            }else{
                return new ResponseEntity("작성자만 삭제/수정할 수 있습니다.",HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("토큰이 유효하지 않습니다.",HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    public ResponseEntity<Map<String,HttpStatus>> deleteComment(Long id, HttpServletRequest request) {

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return new ResponseEntity("토큰이 유효하지 않습니다.",HttpStatus.BAD_REQUEST);
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            if(comment.getUser().getId() == user.getId() || user.getRole() == UserRoleEnum.ADMIN){
                commentRepository.deleteById(id);
                return new ResponseEntity("댓글을 삭제 했습니다.", HttpStatus.OK);
            }else{
                return new ResponseEntity("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity("토큰이 유효하지 않습니다.",HttpStatus.BAD_REQUEST);
        }
    }
}
