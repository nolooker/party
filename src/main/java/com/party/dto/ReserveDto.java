package com.party.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class ReserveDto {
    // 상세 페이지에서 넘어 오는 id와 구매 수량

    private Long productId ;

    @Min(value = 1, message = "최소 이용 가능 시간은 1시간 입니다.")
    @Max(value = 24, message = "하루 최대 이용 시간은 24시간 입니다.")
    private int count ;

    @NotNull(message = "날짜를 선택해 주세요!")
    private String startTime;


    private String endTime;

    public LocalDateTime getStartTimeAsLocalDateTime() {
        return LocalDateTime.parse(startTime);
    }

    public LocalDateTime getEndTimeAsLocalDateTime() {
        return LocalDateTime.parse(endTime);
    }

    private Integer personnel; // 인원수

    private String req; // 요청사항
}
