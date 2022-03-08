package com.moon.aza.validator;

import com.moon.aza.dto.SignUpForm;
import com.moon.aza.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignUpFormValidator implements Validator {
    private final MemberRepository memberRepository;

    /* SignUpForm 일때만 검증 수행 */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }
    /* 검증 수행  */
    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        // 중복 여부 체크
        if(memberRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()},
                    "이미 사용중인 이메일입니다.");
        }
        if(memberRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("nickname","invalid.nickname",new Object[]{signUpForm.getNickname()},
                    "이미 사용중인 닉네임입니다.");
        }
    }
}
