package com.sparta.hanghaeblog.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;

@Setter
@Getter
@Component
public class SignupRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$",message = "유효하지 않은 아이디 형식 입니다.")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$",message = "유효하지 않은 비밀번호 형식 입니다.")
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}