package com.moon.aza.dto;

import com.moon.aza.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long uid;

    @NotBlank(message = "아이디를 반드시 입력해주세요.")
    private String id;

    @NotBlank(message = "비밀번호를 반드시 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,12}$", message = "닉네임은 특수문자를 제외한 2~12자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일을 반드시 입력해주세요!")
    //@Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String certified;


}
