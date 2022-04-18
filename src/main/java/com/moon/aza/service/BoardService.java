package com.moon.aza.service;

import com.moon.aza.dto.BoardForm;
import com.moon.aza.dto.PageRequestDTO;
import com.moon.aza.dto.PageResultDTO;
import com.moon.aza.entity.Board;
import com.moon.aza.entity.Member;
import com.moon.aza.entity.QBoard;
import com.moon.aza.repository.BoardRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // Querydsl 검색 처리
    public BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QBoard qBoard = QBoard.board;
        BooleanExpression expression = qBoard.id.gt(0L); // 0보다 큰 id값 조건 생성
        booleanBuilder.and(expression);

        // 검색 조건이 없는 경우
        if(type==null || type.trim().length()==0)
            return booleanBuilder;

        // 검색 조건
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) // 제목
            conditionBuilder.or(qBoard.title.contains(keyword));
        if(type.contains("c")) // 내용
            conditionBuilder.or(qBoard.contents.contains(keyword));
        if(type.contains("w")) // 작성자
            conditionBuilder.or(qBoard.writer.contains(keyword));

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

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
        //Optional<Board> result = boardRepository.findById(id);
        List<Object[]> result = boardRepository.getBoardWithAll(id);
        Board board = (Board) result.get(0)[0];
        Long commentCnt = (Long) result.get(0)[1];
        Long likesCnt = (Long) result.get(0)[2];

        return entityToDto(board, commentCnt, likesCnt);
    }
    // 게시글 수정
    @Transactional
    public void modify(BoardForm boardForm){
        Optional<Board> board = boardRepository.findById(boardForm.getId());

        if(board.isPresent()){
            Board modifyBoard = board.get();

            modifyBoard.modifyTitle(boardForm.getTitle());
            modifyBoard.modifyContents(boardForm.getContents());

            boardRepository.save(modifyBoard);
        }
    }
    @Transactional
    public void remove(Long id){
        boardRepository.deleteById(id);
    }

    // 페이징 처리
    public PageResultDTO<BoardForm, Object[]> getList(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        //Page<Board> result = boardRepository.findAll(pageable);
        //Page<Object[]> result = boardRepository.getListPage(pageable);
        Page<Object[]> result = boardRepository.searchBoard(booleanBuilder, pageable);

        Function<Object[], BoardForm> fn = (arr -> entityToDto(
                (Board) arr[0],
                (Long) arr[1],
                (Long) arr[2]
        ));

        return new PageResultDTO<>(result, fn);
    }
    // entity -> dto
    public BoardForm entityToDto(Board board, Long commentCnt, Long likesCnt){
        BoardForm dto = BoardForm.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getRegDate())
                .build();
        dto.setCommentCnt(commentCnt.intValue());
        dto.setLikesCnt(likesCnt.intValue());
        return dto;
    }
}
