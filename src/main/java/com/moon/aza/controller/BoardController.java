package com.moon.aza.controller;

import com.moon.aza.entity.Member;
import com.moon.aza.support.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
public class BoardController {
    /* 글 작성 페이지 */
    @GetMapping("/board/register")
    public String boardRegister(@CurrentMember Member member, Model model){
        log.info("/board/register");
        model.addAttribute("member",member);
        return "/aza/board-register";
    }

}
