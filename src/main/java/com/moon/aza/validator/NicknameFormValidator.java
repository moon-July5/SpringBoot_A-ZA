package com.moon.aza.validator;

import com.moon.aza.dto.NicknameForm;
import com.moon.aza.entity.Member;
import com.moon.aza.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class NicknameFormValidator implements Validator {
    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) target;
        Member member = memberRepository.findByNickname(nicknameForm.getNickname());
        if(member != null){
            errors.rejectValue("nickname", "wrong.value","이미 사용중인 닉네임입니다.");
        }
    }
}
