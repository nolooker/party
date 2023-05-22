package com.party.dto;

import com.party.constant.ProductStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class HostProductDto {
    private Long id;
    private String name;
    private String description;
    private String fit;
    private Integer useTime;
    private String imageUrl;
    private Integer price;
    private ProductStatus productStatus;


    @QueryProjection //Projection은 테이블의 특정 컬럼 정보를 조회하는 동작을 말합니다/.
    //해당 조회 결과를 dto에 대입해 줍니다.
    public HostProductDto(Long id, String name, String description, String fit, Integer useTime , String imageUrl, Integer price, ProductStatus productStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fit = fit;
        this.useTime = useTime;
        this.imageUrl = imageUrl;
        this.price = price;
        this.productStatus = productStatus;
    }
}
