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
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long productId ;

    @Min(value = 1, message = "최소 이용 가능 시간은 1시간 입니다.")
    @Max(value = 24, message = "하루 최대 이용 시간은 24시간 입니다.")
    private int count ;

    @NotNull(message = "시작 시간은 필수 입력 값입니다.")
    private String startTime;

    @NotNull(message = "종료 시간은 필수 입력 값입니다.")
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
