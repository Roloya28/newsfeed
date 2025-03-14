package com.example.newsfeed.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequestDto {

    @Email(message = "이메일 형식이 일치하지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 최소 8자, 최대 16자의 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야합니다.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
