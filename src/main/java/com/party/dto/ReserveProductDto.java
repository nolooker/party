package com.party.dto;

import com.party.entity.ReserveProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 주문한 상품 1개에 대한 정보를 저장해주는 클래스
@Getter @Setter
public class ReserveProductDto {
    private String name ;
    private int count ;
    private int reservePrice ;
    private String imageUrl ;
    private Integer personnel; // 인원수
    private LocalDateTime orderDate; // 사용 일자

    
    public ReserveProductDto(ReserveProduct reserveProduct, String imageUrl) {
        this.name = reserveProduct.getProduct().getName() ;
        this.count = reserveProduct.getCount() ;
        this.reservePrice = reserveProduct.getReservePrice() ;
        this.imageUrl = imageUrl;
        this.personnel = reserveProduct.getReserve().getPersonnel();  // 체크
        this.orderDate=reserveProduct.getReserve().getorderDate();

    }
}
