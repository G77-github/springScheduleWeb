package com.example.EsaySchedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    @NotBlank(message = "사용자 이름은 필수입니다")
    private String userName;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String userEmail;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String userPassword;

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String userPasswordCheck;
}
