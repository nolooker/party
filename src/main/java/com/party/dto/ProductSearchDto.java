package com.party.dto;

import com.party.constant.ProductStatus;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ProductSearchDto {

    private String searchDateType ;

    private ProductStatus productStatus ;

    private String searchBy ;

    private String searchQuery ;

	private Long id;


    public void setMemberId(Long memberId) {
    }


    public void setCreatedBy(String loggedInUsername) {
    }


}
