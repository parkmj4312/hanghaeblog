package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.dto.CommentRequestDto;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.service.BoardService;
import com.sparta.hanghaeblog.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
