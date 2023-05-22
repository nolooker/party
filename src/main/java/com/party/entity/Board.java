package com.party.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class Board extends BaseTimeEntity{

    private Long id;

    @NotBlank(message = "작성자를 반드시 입력해 주세요.")
    private String writer;

    @NotBlank(message = "글 제목을 반드시 입력해 주세요.")
    private String title;

    @NotBlank(message = "글 내용을 반드시 입력해 주세요.")
    private String content;

//    private LocalDateTime regDate;
//
//    private LocalDateTime updateDate;
}