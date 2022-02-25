package com.moon.aza.validator;

import com.moon.aza.dto.UserDTO;
import com.moon.aza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class IdCheckValidator extends AbstractValidator<UserDTO> {
    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserDTO dto, Errors errors) {
        if(userRepository.existsById(dto.getId())){
            errors.rejectValue("id","아이디 중복 오류","이미 사용중인 아이디 입니다.");
        }
    }
}
