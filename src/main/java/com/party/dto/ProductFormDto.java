package com.party.dto;

import com.party.constant.ProductStatus;
import com.party.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class ProductFormDto {

    private Long id;

    @NotBlank(message ="파티룸명 은 필수 입력 사항입니다.")
    private String name;

    @NotNull(message ="파티룸 가격은 필수 입력 사항입니다.")
    private Integer price;

    @NotBlank(message = "파티룸 수용인원은 필수 입력 사항입니다.")
    private String fit;

    @NotNull(message = "사용 가능 시간을 입력해주세요.")
    private Integer useTime;

    @NotBlank(message = "파티룸 상세설명은 필수 입력 사항입니다.")
    private String description;

    private ProductStatus productStatus;

    @NotBlank(message = "환불규정은 필수 입력 사항입니다.")
    private String refund; // 환불 규정

    @NotBlank(message = "시설 안내는 필수 입력 사항입니다.")
    private String guide; // 시설안내

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private String address; // 주소

    //파티룸 1개에 최대 이미지
    private List<ProductImageDto>productImageDtoList =new ArrayList<>();

    // 이미지들의 id를 저장할 컬렉션(이미지 수정시 필요함)
    private List<Long> productImageIds=new ArrayList<>();

    private static ModelMapper modelMapper =new ModelMapper();

    public Product createProduct(){
        return modelMapper.map(this, Product.class);
    }

    public static ProductFormDto of(Product product){
        return modelMapper.map(product,ProductFormDto.class);
    }


}
