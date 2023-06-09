package com.party.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8,max = 12,message = "비밀번호는 8자이상, 12자 이하로 입력해 주세요")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotEmpty(message = "휴대폰 번호는 필수 입력 값입니다.")
    private String phone;

    @NotEmpty(message = "가입 유형은 필수 입력 값입니다.")
    private String category;


    private LocalDateTime regdate; // check

}
