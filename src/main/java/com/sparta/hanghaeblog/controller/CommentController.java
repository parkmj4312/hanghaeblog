package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.dto.CommentRequestDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.service.BoardService;
import com.sparta.hanghaeblog.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;
    @ResponseBody
    @PostMapping("/api/boards/{id}/comments")
    public CommentResponseDto createComment(@PathVariable Long id,@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        CommentResponseDto comment = commentService.createComment(requestDto,id,request);
        return comment;
    }
    @PutMapping("/api/boards/comment/{id}")
    public ResponseEntity<Map<String, HttpStatus>> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.update(id, requestDto, request);
    }

    @DeleteMapping("/api/boards/comment/{id}")
    public ResponseEntity<Map<String, HttpStatus>> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity<Map<String,HttpStatus>> responseEntity = commentService.deleteComment(id,request);
        return responseEntity;
    }

}
