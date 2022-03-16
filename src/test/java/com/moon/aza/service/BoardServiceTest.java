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
    public void pageTest(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<BoardForm, Board> resultDTO = boardService.getList(pageRequestDTO);

        for(BoardForm boardForm : resultDTO.getDtoList()){
            System.out.println(boardForm);
        }
    }
}