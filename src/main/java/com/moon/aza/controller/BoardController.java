package com.moon.aza.controller;

import com.moon.aza.dto.BoardForm;
import com.moon.aza.dto.PageRequestDTO;
import com.moon.aza.entity.Board;
import com.moon.aza.entity.Member;
import com.moon.aza.service.BoardService;
import com.moon.aza.support.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;


    /* 글 작성 페이지 */
    @GetMapping("/board/register")
    public String boardRegister(@CurrentMember Member member, Model model){
        log.info("/board/register");
        model.addAttribute("member",member);
        model.addAttribute(new BoardForm());
        return "/aza/board-register";
    }
    @GetMapping("/board/read")
    public String boardRead(@CurrentMember Member member, long id, PageRequestDTO pageRequestDTO, Model model){
        log.info("/board/read");
        BoardForm boardForm = boardService.read(id);
        model.addAttribute("boardForm", boardForm);
        if(member != null)
            model.addAttribute(member);
        return "/aza/board-read";
    }

    @PostMapping("/board/register")
    public String register(@ModelAttribute BoardForm boardForm, RedirectAttributes redirectAttributes){
        Board board = boardService.register(boardForm);

        return "redirect:/board";
    }

}
