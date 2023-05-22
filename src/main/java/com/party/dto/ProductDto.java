package com.party.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class ProductDto {
    private Long id;
    private String name;
    private String fit;
    private Integer useTime;
    private String description;
    private String notStad;
    private LocalDateTime regdate;
    private LocalDateTime updateDate;
    private String refund; // 환불 규정
    private String guide; // 시설 안내
    private String address; // 주소

}
