package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.security.UserDetailsImpl;
import com.sparta.hanghaeblog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public List<BoardResponseDto> home() {
        //return new ModelAndView("index");
        return boardService.getBoards();
    }

    @ResponseBody
    @PostMapping("/api/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto board = boardService.createBoard(requestDto, userDetails.getUser());
        return board;
    }

    @GetMapping("/api/boards")
    public List<BoardResponseDto> getBoards() {
        List<BoardResponseDto> boards = boardService.getBoards();
        return boards;
    }
    @GetMapping("/api/boards/{id}")
    public BoardResponseDto selectBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }
    @PutMapping("/api/boards/{id}")
    public ResponseEntity<Map<String, HttpStatus>> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.update(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<Map<String, HttpStatus>> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return boardService.deleteBoard(id,userDetails.getUser());
    }

    @PutMapping("/api/boards/{id}/loves")
    public ResponseEntity<Map<String, HttpStatus>> BoardLoveOk(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.loveOk(id, userDetails.getUser());
    }
}