package com.moon.aza.controller;

import com.moon.aza.dto.UserDTO;
import com.moon.aza.entity.User;
import com.moon.aza.repository.UserRepository;
import com.moon.aza.security.jwt.JwtAuthenticationProvider;
import com.moon.aza.service.UserService;
import com.moon.aza.validator.EmailCheckValidator;
import com.moon.aza.validator.IdCheckValidator;
import com.moon.aza.validator.NicknameCheckValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final IdCheckValidator idCheckValidator;
    private final EmailCheckValidator emailCheckValidator;
    private final NicknameCheckValidator nicknameCheckValidator;

    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String signup(UserDTO userDTO) {
        log.info("/user/signup");
        return "/aza/signup";
    }
    // 로그인 페이지
    @GetMapping("/user/login")
    public String login(@RequestParam(value="error", required = false) String error,
                        @RequestParam(value="exception", required = false) String exception,
                        Model model){
        log.info("/user/login");
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "/aza/login";
    }

    // 이메일 인증 페이지
    @GetMapping("/user/emailAuth")
    public String emailAuth(Principal principal, Authentication authentication, Model model){
        log.info("/user/emailAuth");
        log.info("principal : "+principal);
        log.info("authentication : "+authentication.getPrincipal());

        model.addAttribute("user",authentication.getPrincipal());
        return "/aza/emailAuth";
    }
    // 회원가입 로직
    @PostMapping("/user/signup")
    public String userRegister(@Valid UserDTO userDTO, Errors errors, Model model){
        if(errors.hasErrors()){
            // 회원가입 실패 시 입력한 데이터 값 유지
            model.addAttribute("userDTO", userDTO);

            // 유효성 검사를 통과 못한 필드와 메시지
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/aza/signup";
        }
        userService.signupUser(userDTO);

        return "redirect:/user/login";
    }
    /*커스텀 유효성 검증을 위해 사용*/
    @InitBinder // 특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 사용
    public void validatorBinder(WebDataBinder binder){
        binder.addValidators(idCheckValidator);
        binder.addValidators(emailCheckValidator);
        binder.addValidators(nicknameCheckValidator);
    }
}
