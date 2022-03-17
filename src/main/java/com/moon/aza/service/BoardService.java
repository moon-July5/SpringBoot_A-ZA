package com.moon.aza.service;

import com.moon.aza.dto.BoardForm;
import com.moon.aza.dto.PageRequestDTO;
import com.moon.aza.dto.PageResultDTO;
import com.moon.aza.entity.Board;
import com.moon.aza.entity.Member;
import com.moon.aza.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 작성 로직
    public Board register(BoardForm boardForm){
        Map<String, Object> entityMap = saveBoard(boardForm);
        Board board = (Board) entityMap.get("board");

        return boardRepository.save(board);
    }
    // 게시글 저장
    public Map<String, Object> saveBoard(BoardForm boardForm){
        Map<String, Object> entityMap = new HashMap<>();
        Board board = Board.builder()
                .title(boardForm.getTitle())
                .contents(boardForm.getContents())
                .writer(boardForm.getWriter())
                .member(Member.builder().id(boardForm.getId()).build())
                .build();
        entityMap.put("board", board);
        return entityMap;
    }
    // 게시글 조회
    public BoardForm read(Long id){
        Optional<Board> result = boardRepository.findById(id);

        return result.isPresent()? entityToDto(result.get()):null;
    }
    // 페이징 처리
    public PageResultDTO<BoardForm, Board> getList(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("id"));

        Page<Board> result = boardRepository.findAll(pageable);

        Function<Board, BoardForm> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }
    // entity -> dto
    public BoardForm entityToDto(Board board){
        BoardForm dto = BoardForm.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getRegDate())
                .build();
        return dto;
    }
}
