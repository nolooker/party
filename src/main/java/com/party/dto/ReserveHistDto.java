package com.party.dto;

import com.party.constant.ReserveStatus;
import com.party.entity.Reserve;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ReserveHistDto {
    private Long reserveId ; // 예약 아이디
    private String reserveDate ; // 예약 일자
    private ReserveStatus reserveStatus ; // 예약 상태 정보

    // 시작 시간
    private String startTime;

    // 종료 시간
    private String endTime;

    private Integer personnel ;

    private String req ;

    // 주문 상품 리스트
    private List<ReserveProductDto> reserveProductDtoList = new ArrayList<>() ;

    public void addOrderProductDto(ReserveProductDto reserveProductDto){
        reserveProductDtoList.add(reserveProductDto);
    }

    public ReserveHistDto(Reserve reserve) {
        this.reserveId = reserve.getId() ;


        String pattern = "yyyy-MM-dd HH:mm" ;
        this.reserveDate =reserve.getorderDate().format(DateTimeFormatter.ofPattern(pattern));
        this.startTime=reserve.getStartTime().format(DateTimeFormatter.ofPattern(pattern));
        this.endTime=reserve.getEndTime().format(DateTimeFormatter.ofPattern(pattern));
        this.personnel=reserve.getPersonnel();
        this.req=reserve.getReq();


        this.reserveStatus = reserve.getReserveStatus() ;
    }

    public void addReserveProductDto(ReserveProductDto reserveProductDto) {

        reserveProductDtoList.add(reserveProductDto);
    }
}
