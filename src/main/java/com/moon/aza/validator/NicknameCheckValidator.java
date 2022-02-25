package com.moon.aza.validator;

import com.moon.aza.dto.UserDTO;
import com.moon.aza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class NicknameCheckValidator extends AbstractValidator<UserDTO> {
    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserDTO dto, Errors errors) {
        if(userRepository.existsByNickname(dto.getNickname())){
            errors.rejectValue("nickname","닉네임 중복 오류","이미 사용중인 닉네임 입니다.");
        }
    }
}
