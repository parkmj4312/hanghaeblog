package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.CommentRequestDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.security.UserDetailsImpl;
import com.sparta.hanghaeblog.service.BoardService;
import com.sparta.hanghaeblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;
    @ResponseBody
    @PostMapping("/api/boards/{id}/comments")
    public CommentResponseDto createComment(@PathVariable Long id,@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto comment = commentService.createComment(requestDto,id,userDetails.getUser());
        return comment;
    }
    @PutMapping("/api/boards/comment/{id}")
    public ResponseEntity<Map<String, HttpStatus>> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.update(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/api/boards/comment/{id}")
    public ResponseEntity<Map<String, HttpStatus>> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<Map<String,HttpStatus>> responseEntity = commentService.deleteComment(id,userDetails.getUser());
        return responseEntity;
    }

}
