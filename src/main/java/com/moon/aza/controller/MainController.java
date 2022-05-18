package com.moon.aza.controller;

import com.moon.aza.dto.PageRequestDTO;
import com.moon.aza.dto.SignUpForm;
import com.moon.aza.entity.Member;
import com.moon.aza.service.BoardService;
import com.moon.aza.service.LikesService;
import com.moon.aza.support.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MainController {
    private final BoardService boardService;
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
    @GetMapping("/board") // 화면에서 page 와 size를 전달받기 위해
    public String board(PageRequestDTO pageRequestDTO, Model model) {
        log.info("/board");
        model.addAttribute("result", boardService.getList(pageRequestDTO));
        return "/aza/board";
    }
    /* 내가 쓴 게시글 */
    @GetMapping("/myboard")
    public String myBoard(PageRequestDTO pageRequestDTO,@CurrentMember Member member ,Model model){
        log.info("/myboard");
        model.addAttribute("result", boardService.getMyList(pageRequestDTO, member.getId()));
        return "/aza/myboard";
    }
}
