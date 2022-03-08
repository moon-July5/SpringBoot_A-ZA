package com.moon.aza.controller;

import com.moon.aza.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JavaMailSender mailSender;

    @Test
    @DisplayName("회원 가입 화면")
    void signup() throws Exception{
        mockMvc.perform(get("/member/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/aza/signup"))
                .andExpect(model().attributeExists("signUpForm"));

    }
    @Test
    @DisplayName("회원 가입 처리: 입력값 오류")
    void signUpSubmitWithError() throws Exception {
        mockMvc.perform(post("/member/signup")
                        .param("nickname", "nickname")
                        .param("email", "email@gmail")
                        .param("password", "1234!")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/aza/signup"));
    }

    @Test
    @DisplayName("회원 가입 처리: 입력값 정상")
    void signUpSubmit() throws Exception {
        mockMvc.perform(post("/member/signup")
                        .param("nickname", "nickname")
                        .param("email", "email@gmail.com")
                        .param("password", "1234!@#$")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/aza/home"));

        // 메일이 DB에 저장되었는지 확인
        assertTrue(memberRepository.existsByEmail("rudtjr0705@naver.com"));

//        then(mailSender).should().send(any(SimpleMailMessage.class));
    }
}