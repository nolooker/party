package com.party.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
public class HostReviewDto {
    private Long id;
    private String name;
    private String title;
    private String description;
    private String imageUrl;
    private LocalDateTime regdate;
    private String createBy;

//    private String fit;
//    private Integer useTime;
//    private Integer price;
//    private ReviewStatus reviewStatus ;

    @QueryProjection //Projection은 테이블의 특정 컬럼 정보를 조회하는 동작을 말합니다/.
    //해당 조회 결과를 dto에 대입해 줍니다.

    public HostReviewDto(Long id, String name, String title, String description, String imageUrl, LocalDateTime regdate, String createBy) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.regdate = regdate;
        this.createBy = createBy;
    }


//        this.fit = fit;
//        this.useTime = useTime;
//        this.price = price;
//        this.reviewStatus = reviewStatus;
    }

