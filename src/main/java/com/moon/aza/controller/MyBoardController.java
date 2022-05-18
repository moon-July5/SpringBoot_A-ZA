package com.moon.aza.controller;

import com.moon.aza.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/myboard")
@Controller
public class MyBoardController {
    private final BoardService boardService;

    @ResponseBody
    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestBody List<String> ids){
        log.info("ids : {}",ids);
        boardService.checkRemove(ids);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
