package com.party.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class MainReviewDto {
    private Long id;
    private String name;
    private String title;
    private String description;
    private String imageUrl;
    private int rating;


//    private String fit;
//    private Integer useTime;
//    private Integer price;
//    private ReviewStatus reviewStatus;

    @QueryProjection //Projection은 테이블의 특정 컬럼 정보를 조회하는 동작을 말합니다/.
    //해당 조회 결과를 dto에 대입해 줍니다.
    public MainReviewDto(Long id, String name, String title, String description, String imageUrl, int rating) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating ;
//        this.fit = fit;
//        this.useTime = useTime;
//        this.price = price;
//        this.reviewStatus = reviewStatus;
    }
}
