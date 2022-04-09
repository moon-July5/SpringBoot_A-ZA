package com.moon.aza.controller;

import com.moon.aza.dto.NicknameForm;
import com.moon.aza.dto.PasswordForm;
import com.moon.aza.entity.Member;
import com.moon.aza.service.MemberService;
import com.moon.aza.support.CurrentMember;
import com.moon.aza.validator.NicknameFormValidator;
import com.moon.aza.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class SettingController {
    private final NicknameFormValidator nicknameFormValidator;
    private final PasswordFormValidator passwordFormValidator;
    private final MemberService memberService;

    // 패스워드 변경
    @GetMapping("/settings/password")
    public void passwordForm(@CurrentMember Member member, Model model){
        model.addAttribute(member);
        model.addAttribute(new PasswordForm());
    }
    // 패스워드 변경
    @PostMapping("/settings/password")
    public String changePassword(@CurrentMember Member member, @Valid PasswordForm passwordForm,
                                 Errors errors, Model model, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            Map<String, String> validatorResult = memberService.validateHandling(errors);

            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/settings/password";
        }

        memberService.changePassword(member, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message","패스워드를 변경했습니다.");
        return "redirect:/settings/password";
    }
    // 닉네임 변경
    @GetMapping("/settings/nickname")
    public void nicknameForm(@CurrentMember Member member, Model model){
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
