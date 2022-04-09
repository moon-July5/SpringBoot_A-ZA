package com.moon.aza.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class NicknameForm {
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    public NicknameForm(String nickname){
        this.nickname = nickname;
    }
}
