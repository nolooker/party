package com.party.entity;

import com.party.constant.ReserveStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="reserve")
@Getter @Setter @ToString
public class Reserve extends BaseEntity2 {

    @Id
    @Column(name = "reserve_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  // 예약 번호

    // 주문시 회원 연동  // 멤버 조인컬럼 같은 지 확인할 것
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member ;


    // mappedBy = "reserve"의 reserve는 OrderProduct 클래스에 들어 있는 변수의 이름입니다.
    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReserveProduct> reserveProducts = new ArrayList<ReserveProduct>();

    @Column
    private Integer personnel; // 인원수

    @Lob // CLOB, BLOB
    @Column(nullable = true)
    private String req; // 요청사항

    @Column
    private LocalDateTime startTime; // 시작 시간

    @Column
    private LocalDateTime endTime; // 종료 시간


    private LocalDateTime orderDate; // 주문 시간

    @Column(name = "create_by")
    private String createBy;

//    @PrePersist
//    private void prePersist() {
//        if (getCreateBy() != null) {
//            setCreateBy(product.getCreateBy());
//        }
//    }




    // could not resolve property: reserveDate of: com.party.entity.Reserve

    // 예약 가능/불가능 상태 확인
    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus ;



    public void addReserveProduct(ReserveProduct reserveProduct){
        reserveProducts.add(reserveProduct);
        reserveProduct.setReserve(this);
    }

    public static Reserve createreserve(Member member,Product product, List<ReserveProduct> reserveProductList,LocalDateTime startTime, LocalDateTime endTime,Integer personnel, String req){
        Reserve reserve = new Reserve();
        reserve.setMember(member);

        for(ReserveProduct bean : reserveProductList){
            reserve.addReserveProduct(bean);
        }

        reserve.setReserveStatus(ReserveStatus.ORDER);
        reserve.setPersonnel(reserve.getPersonnel());  // check
        reserve.setReq(reserve.getReq()); // check
        reserve.setOrderDate(LocalDateTime.now());
        reserve.setCreateBy(product.getCreateBy());



        return reserve ;
    }

    public int getTotalPrice(){
        int totalPrice = 0 ;

        for(ReserveProduct bean : reserveProducts){
            totalPrice += bean.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelReserve(){
        this.reserveStatus = ReserveStatus.CANCEL ;

        for(ReserveProduct bean : reserveProducts){
            bean.cancel();
        }
    }

    public LocalDateTime getorderDate() {
        // 현재 시간 반환 또는 다른 기본값을 반환
        return Objects.requireNonNullElseGet(orderDate, LocalDateTime::now);
    }



}
