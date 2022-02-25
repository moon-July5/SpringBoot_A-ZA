package com.moon.aza.service;

import com.moon.aza.security.dto.UserAuthDTO;
import com.moon.aza.dto.UserDTO;
import com.moon.aza.entity.User;
import com.moon.aza.entity.UserRole;
import com.moon.aza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입 로직
    @Transactional
    public User signupUser(UserDTO userDTO){
        User user = dtoToEntity(userDTO);
        user.addUserRole(UserRole.ROLE_USER);

        return userRepository.save(user);
    }

    // 회원가입 유효성 검사
    @Transactional
    public Map<String, String> validateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();

        // 유효성 검사에 실패한 필드 목록을 받는다.
        for (FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s",error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    // DTO 객체를 Entity로 변환
    public User dtoToEntity(UserDTO userDTO){
        User entity = User.builder()
                .uid(userDTO.getUid())
                .id(userDTO.getId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .nickname(userDTO.getNickname())
                .email(userDTO.getEmail())
                .certified(certifiedKey())
                .build();
        return entity;
    }

    // 10자리의 임의의 문자열을 만드는 함수
    private String certifiedKey() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;
        while (sb.length() < 10) {
            num = random.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }

        }
        return sb.toString();
    }
}
