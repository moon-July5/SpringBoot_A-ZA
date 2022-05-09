package com.moon.aza.controller;

import com.moon.aza.dto.NicknameForm;
import com.moon.aza.dto.PasswordForm;
import com.moon.aza.entity.Member;
import com.moon.aza.service.MemberService;
import com.moon.aza.support.CurrentMember;
import com.moon.aza.validator.NicknameFormValidator;
import com.moon.aza.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Log4j2
@RequiredArgsConstructor
@Controller
public class SettingController {
    private final NicknameFormValidator nicknameFormValidator;
    private final PasswordFormValidator passwordFormValidator;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    // 회원 탈퇴
    @GetMapping("/settings/member-delete")
    public void memberForm(@CurrentMember Member member, Model model){
        log.info("/settings/member-delete");
        model.addAttribute(member);
    }
    // 회원 탈퇴
    @PostMapping("/settings/member-delete")
    public String memberDelete(@CurrentMember Member member, String email, String password,
                               HttpSession httpSession, Model model){
        Member result = memberService.findMemberByEmail(email);
        if(result==null || !passwordEncoder.matches(password, member.getPassword())){
            model.addAttribute("error", "유효하지 않은 정보입니다.");
            return "/settings/member-delete";
        }
        memberService.deleteMember(member);
        httpSession.invalidate();
        return "redirect:/";
    }

    // 패스워드 변경
    @GetMapping("/settings/password")
    public void passwordForm(@CurrentMember Member member, Model model){
        log.info("/settings/password");
        model.addAttribute(member);
        model.addAttribute(new PasswordForm());
    }

    // 패스워드 변경
    @PostMapping("/settings/password")
    public String changePassword(@CurrentMember Member member, @Valid PasswordForm passwordForm,
                                 Errors errors, Model model, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            model.addAttribute(member);
            return "/settings/password";
        }

        memberService.changePassword(member, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message","패스워드를 변경했습니다.");
        return "redirect:/settings/password";
    }
    // 닉네임 변경
    @GetMapping("/settings/nickname")
    public void nicknameForm(@CurrentMember Member member, Model model){
        log.info("/settings/nickname");
        model.addAttribute(member);
        model.addAttribute(new NicknameForm(member.getNickname()));

    }
    // 닉네임 변경
    @PostMapping("/settings/nickname")
    public String changeNickname(@CurrentMember Member member, @Valid NicknameForm nicknameForm,
                                 Errors errors, Model model, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            model.addAttribute(member);
            return "/settings/nickname";
        }
        memberService.changeNickname(member, nicknameForm.getNickname());
        redirectAttributes.addFlashAttribute("message","닉네임을 수정하였습니다.");
        return "redirect:/settings/nickname";
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder){ webDataBinder.addValidators(nicknameFormValidator);}

    @InitBinder("passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) { webDataBinder.addValidators(passwordFormValidator);}
}
