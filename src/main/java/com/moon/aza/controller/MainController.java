package com.moon.aza.controller;

import com.moon.aza.dto.SignUpForm;
import com.moon.aza.entity.Member;
import com.moon.aza.support.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MainController {

    /* 홈(메인) 페이지 */
    @GetMapping("/")
    public String home(@CurrentMember Member member, Model model) {
        log.info("/");
        if(member != null)
            model.addAttribute(member);

        return "/aza/home";
    }
    /* 회원가입 페이지 */
    @GetMapping("/signup")
    public String signup(Model model) {
        log.info("/signup");
        model.addAttribute(new SignUpForm());
        return "/aza/signup";
    }
    /* 로그인 페이지 */
    @GetMapping("/login")
    public String login(){
        log.info("/login");
        return "/aza/login";
    }
    /* 게시판 페이지 */
    @GetMapping("/board")
    public String board() {
        log.info("/aza/board");
        return "/aza/board";
    }

}
