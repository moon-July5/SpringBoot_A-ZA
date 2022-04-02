package com.moon.aza.service;

import com.moon.aza.dto.BoardForm;
import com.moon.aza.dto.PageRequestDTO;
import com.moon.aza.dto.PageResultDTO;
import com.moon.aza.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {
    @Autowired
    public BoardService boardService;

    @Test
    public void pageTest1(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

//        PageResultDTO<BoardForm, Board> resultDTO = boardService.getList(pageRequestDTO);
//
//        for(BoardForm boardForm : resultDTO.getDtoList()){
//            System.out.println(boardForm);
//        }
    }

    @Test
    public void pageTest2() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

//        PageResultDTO<BoardForm, Board> resultDTO = boardService.getList(pageRequestDTO);
//
//        System.out.println("PREV: "+resultDTO.isPrev());
//        System.out.println("NEXT: "+resultDTO.isNext());
//        System.out.println("TOTAL: "+resultDTO.getTotalPage());
//        System.out.println("==================================");
//
//        for(BoardForm boardForm : resultDTO.getDtoList()){
//            System.out.println(boardForm);
//        }
//        System.out.println("==================================");
//        // 화면에 출력될 페이지 번호
//        resultDTO.getPageList().forEach(i -> System.out.println(i));

    }
}