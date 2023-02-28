package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.service.BoardService;
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

    @PostMapping("/api/boards")
    public Board createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }

    @GetMapping("/api/boards")
    public List<Board> getBoards() {
        return boardService.getBoards();
    }
    @GetMapping("/api/boards/{id}")
    public Optional<Board> selectBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }
    @PutMapping("/api/boards/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    @DeleteMapping("/api/boards/{id}")
    public String deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.deleteBoard(id, requestDto);
    }

}