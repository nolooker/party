package com.party.dto;

import com.party.entity.ReviewImage;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ReviewImageDto {

    private Long id;

    private String imageName; // 이미지 이름(uuid)
    private String oriImageName; // 원본 이미지 이름
    private String imageUrl; // 이미지 조회 경로
    private String repImageYesNo; // 대표 이미지 여부

    private static ModelMapper modelMapper=new ModelMapper();

    public static ReviewImageDto of(ReviewImage reviewImage){
        //입력되는 ReviewImage 엔티티 정보 이용하여 dto객체에 매핑

        return modelMapper.map(reviewImage, ReviewImageDto.class);
    }
}
