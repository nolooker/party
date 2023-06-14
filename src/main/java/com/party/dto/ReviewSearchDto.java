package com.party.dto;

import com.party.constant.ReviewStatus;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ReviewSearchDto {

    private String searchDateType ;

    private ReviewStatus reviewStatus;

    private String searchBy ;

    private String searchQuery ;

	private Long id;



}
