package com.party.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ReserveProduct extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "reserve_product_id")
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Product product ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_id")
    private Reserve reserve ;

    private int reservePrice ; // 예약 가격

    private int count ; // 시간을 수량으로 계산


    private Integer personnel; // 인원수

    private String req; // 요청사항


    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public static ReserveProduct createReserveProduct(Product product, int count,LocalDateTime startTime, LocalDateTime endTime,Integer personnel,String req) {

        ReserveProduct reserveProduct = new ReserveProduct();
        reserveProduct.setProduct(product);
        reserveProduct.setCount(count);
        reserveProduct.setReservePrice(product.getPrice());
        reserveProduct.setStartTime(startTime);
        reserveProduct.setEndTime(endTime);
        reserveProduct.setPersonnel(personnel);
        reserveProduct.setReq(req);

        product.removeStock(count);

        return reserveProduct ;

    }

    public int getTotalPrice() { // 결제 총 금액
        return reservePrice * count ;
    }

    public void cancel() {
        this.getProduct().addUseTime(count);
    }
}
