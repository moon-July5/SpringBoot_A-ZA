package com.moon.aza.validator;

import com.moon.aza.dto.UserDTO;
import com.moon.aza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class EmailCheckValidator extends AbstractValidator<UserDTO> {
    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserDTO dto, Errors errors) {
        if(userRepository.existsByEmail(dto.getEmail())){
            errors.rejectValue("email", "이메일 중복 오류","이미 사용중인 이메일 입니다.");
        }
    }
}
