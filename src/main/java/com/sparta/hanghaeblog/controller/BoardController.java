package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.dto.BoardResponseDto;
import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public List<Board> home() {
        //return new ModelAndView("index");
        return boardService.getBoards();
    }

    @ResponseBody
    @PostMapping("/api/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        BoardResponseDto board = boardService.createBoard(requestDto, request);
        return board;
    }

    @GetMapping("/api/boards")
    public List<Board> getBoards() {
        return boardService.getBoards();
    }
    @GetMapping("/api/boards/{id}")
    public Board selectBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }
    @PutMapping("/api/boards/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.update(id, requestDto, request);
    }

    @DeleteMapping("/api/boards/{id}")
    public String deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {

        return new String(boardService.deleteBoard(id, requestDto,request));
    }



}