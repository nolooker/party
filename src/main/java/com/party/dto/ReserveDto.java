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

    private String startTime;

    private String endTime;

    private Integer personnel ;

    private String req ;

    public LocalDateTime getStartTimeAsLocalDateTime() {
        if (startTime != null) {
            return LocalDateTime.parse(startTime);
        }
        // startTime이 null인 경우 예외 처리를 수행하거나 기본값을 반환하도록 수정해주세요.
        // 예를 들어, 기본값인 LocalDateTime.now()를 반환하려면 아래와 같이 수정할 수 있습니다.
        // return LocalDateTime.now();
        throw new IllegalArgumentException("startTime is null");
    }

    public LocalDateTime getEndTimeAsLocalDateTime() {
        if (endTime != null) {
            return LocalDateTime.parse(endTime);
        }
        // endTime이 null인 경우 예외 처리를 수행하거나 기본값을 반환하도록 수정해주세요.
        // 예를 들어, 기본값인 LocalDateTime.now()를 반환하려면 아래와 같이 수정할 수 있습니다.
        // return LocalDateTime.now();
        throw new IllegalArgumentException("endTime is null");
    }

}
