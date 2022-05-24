package com.moon.aza.controller;

import com.moon.aza.dto.SignUpForm;
import com.moon.aza.entity.Member;
import com.moon.aza.service.MemberService;
import com.moon.aza.support.CurrentMember;
import com.moon.aza.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {
    private final SignUpFormValidator signUpFormValidator;
    private final MemberService memberService;

    /*이메일 로그인 페이지*/
    @GetMapping("/email-login")
    public String emailLogin(){
        log.info("/member/email-login");
        return "aza/email-login";
    }
    @PostMapping("/email-login")
    public String sendLinkForEmailLogin(String email, Model model, RedirectAttributes redirectAttributes) throws MessagingException{
        Member member = memberService.findMemberByEmail(email);
        if(member==null){
            model.addAttribute("error","유효한 이메일 주소가 아닙니다.");
            return "aza/email-login";
        }
        if(!member.enableToSendEmail()){
            model.addAttribute("error","이메일 전송은 5분에 한 번만 전송할 수 있습니다.");
            return "aza/email-login";
        }
        memberService.sendLoginLink(member);
        redirectAttributes.addFlashAttribute("message","로그인 가능한 링크를 성공적으로 이메일로 전송하였습니다.");
        return "redirect:/member/email-login";
    }
    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model){
        Member member = memberService.findMemberByEmail(email);
        if(member==null || !member.isValid(token)){
            model.addAttribute("error","로그인 할 수 없습니다.");
            return "aza/logged-in-by-email";
        }
        memberService.login(member);
        return "aza/logged-in-by-email";
    }

    /* 이메일 인증 페이지*/
    @GetMapping("/email-check")
    public String emailCheck(@CurrentMember Member member, Model model){
        model.addAttribute("email", member.getEmail());
        return "aza/email-check";
    }
    /* 이메일 재전송 */
    @GetMapping("/email-resend")
    public String emailResend(@CurrentMember Member member, Model model) throws MessagingException {
        if(!member.enableToSendEmail()){
            model.addAttribute("error", "인증 이메일은 5분에 한 번만 전송할 수 있습니다.");
            model.addAttribute("email", member.getEmail());
            return "aza/email-check";
        }
        memberService.sendVerificationEmail(member);
        return "redirect:/";
    }
    /* 회원가입 로직 */
    @PostMapping("/signup") /* @Valid - 타입에 대한 검증 */
    public String memberRegister(@Valid @ModelAttribute SignUpForm signUpForm,
                                 Errors errors, Model model) throws MessagingException { /* Errors - 에러를 담을 수 있는 객체 */
        // 회원가입 유효성 검사
        if(errors.hasErrors()) {

            Map<String, String> validatorResult = memberService.validateHandling(errors);

            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "aza/signup";
        }

        Member newMember = memberService.signUp(signUpForm);
        // 회원가입 즉시 로그인
        memberService.login(newMember);

        return "redirect:/";
    }

    // 이메일 인증 확인
    @GetMapping("/email-check-token")
    public String verifyEmail(String token, String email, Model model){
        Member member = memberService.findMemberByEmail(email);
        log.info("token : "+token);
        log.info("email : "+email);
        if(member == null){ // 계정정보가 없으면
            model.addAttribute("error", "wrong.email");
            return "aza/email-verify";
        }
        if (!token.equals(member.getEmailToken())) {
            model.addAttribute("error", "wrong.token");
            return "aza/email-verify";
        }

        memberService.verify(member);
        model.addAttribute("nickname",member.getNickname());
        return "aza/email-verify";
    }

    /* 객체 검증*/
    @InitBinder("signUpForm") // attribute로 바인딩할 객체 지정
    public void signUpFormInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }


}