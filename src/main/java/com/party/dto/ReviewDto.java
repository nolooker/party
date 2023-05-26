package com.party.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class ReviewDto {
    private Long id; // 아이디
    private String name; // 이름
    private String title; // 제목
    private String description; // 상세 설명
    private LocalDateTime regdate;
    private LocalDateTime updateDate;

   // private String fit;
   // private Integer useTime;
   // private String notStad;
   // private String refund; // 환불 규정
   // private String guide; // 시설 안내
   // private String address; // 주소

}
