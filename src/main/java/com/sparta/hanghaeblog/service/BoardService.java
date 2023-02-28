package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.BoardRequestDto;
import com.sparta.hanghaeblog.entity.Board;
import com.sparta.hanghaeblog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto);
        boardRepository.save(board);
        return board;
    }

    @Transactional(readOnly = true)
    public List<Board> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Board> getBoard(long id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public Board update(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(board.getPassword().equals(requestDto.getPassword())){
            board.update(requestDto);
            return board;
        }else{
            return board;
        }


    }

    @Transactional
    public String deleteBoard(Long id,BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(board.getPassword().equals(requestDto.getPassword())){
            boardRepository.deleteById(id);
            return "삭제에 성공 했습니다.";
        }else{
            return "비밀번호가 틀렸습니다..";
        }


    }
}
