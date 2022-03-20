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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {
    private final BoardService boardService;


    /* 글 작성 페이지 */
    @GetMapping("/register")
    public void boardRegister(@CurrentMember Member member, Model model){
        log.info("/board/register");
        model.addAttribute("member",member);
        model.addAttribute(new BoardForm());
    }

    @GetMapping({"/read","/modify"})
    public void boardRead(@CurrentMember Member member, Long id, PageRequestDTO pageRequestDTO, Model model){
        log.info("id : "+id);
        BoardForm boardForm = boardService.read(id);
        model.addAttribute("boardForm", boardForm);
        if(member != null)
            model.addAttribute(member);
    }

    @PostMapping("/register")
    public String register(@ModelAttribute BoardForm boardForm){
        Board board = boardService.register(boardForm);

        return "redirect:/board";
    }

    @PutMapping("/modify/{id}")
    public String modify(@ModelAttribute BoardForm boardForm, PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){
        boardService.modify(boardForm);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("id", boardForm.getId());

        return "redirect:/board/read";
    }
    @DeleteMapping("/remove/{id}")
    public String remove(@PathVariable Long id){
        boardService.remove(id);
        return "redirect:/board";
    }

}
