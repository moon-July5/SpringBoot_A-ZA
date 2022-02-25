package com.moon.aza.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/aza")
@Controller
public class MainController {

    // 메인 페이지
    @GetMapping("/index")
    public void index() { log.info("/aza/index"); }

}
