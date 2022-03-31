package com.moon.aza.controller;

import com.moon.aza.dto.CommentDTO;
import com.moon.aza.entity.Comment;
import com.moon.aza.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    // 댓글 목록
    @GetMapping("/{boardId}/all")
    public ResponseEntity<List<CommentDTO>> getList(@PathVariable("boardId") Long boardId){
        log.info("boardId : "+boardId);
        List<CommentDTO> commentList = commentService.getListOfComment(boardId);

        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    // 댓글 저장
    @PostMapping("/{boardId}")
    public ResponseEntity<Long> addComment(@RequestBody CommentDTO commentDTO){
        log.info("CommentDTO : "+commentDTO);
        Long commentNum = commentService.save(commentDTO);
        return new ResponseEntity<>(commentNum, HttpStatus.OK);
    }

}
