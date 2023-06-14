package com.party.dto;

import com.party.entity.Review;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class ReviewFormDto {

    private Long id;

    @NotBlank(message ="파티룸명 은 필수 입력 사항입니다.")
    private String name;

    @NotBlank(message ="후기 제목은 필수 입력 사항입니다.")
    private String title;

    @NotBlank(message = "후기 내용은 필수 입력 사항입니다.")
    @Length(min = 10,max = 50,message = "후기 내용은 50자이상, 자 이하로 입력해 주세요")
    private String description;

    @NotBlank(message = "별점은 필수 입력 사항입니다.")
    private String rating;


//    @NotBlank(message = "파티룸 수용인원은 필수 입력 사항입니다.")
//    private String fit;
//    @NotNull(message = "사용 가능 시간을 입력해주세요.")
//    private Integer useTime;

//    @NotNull(message ="파티룸 가격은 필수 입력 사항입니다.")
//    private Integer price;

//   private ReviewStatus reviewStatus;
//
//   @NotBlank(message = "환불규정은 필수 입력 사항입니다.")
//    private String refund; // 환불 규정
//
//    @NotBlank(message = "시설 안내는 필수 입력 사항입니다.")
//    private String guide; // 시설안내
//
//    @NotBlank(message = "주소는 필수 입력 사항입니다.")
//    private String address; // 주소

    //파티룸 1개에 최대 이미지
    private List<ReviewImageDto>reviewImageDtoList =new ArrayList<>();

    // 이미지들의 id를 저장할 컬렉션(이미지 수정시 필요함)
    private List<Long> reviewImageIds=new ArrayList<>();

    private static ModelMapper modelMapper =new ModelMapper();

    public Review createReview(){
        return modelMapper.map(this, Review.class);
    }

    public static ReviewFormDto of(Review review){
        return modelMapper.map(review, ReviewFormDto.class);
    }


}
