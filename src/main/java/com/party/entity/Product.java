package com.party.entity;

import com.party.constant.ProductStatus;
import com.party.dto.ProductFormDto;
import com.party.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Getter@Setter@ToString
public class Product extends BaseEntity {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(nullable = false,name = "price")
    private Integer price;

    @Column(nullable = false)
    private String fit;

    @Column(nullable = false)
    private Integer useTime;

    @Lob
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Lob
    @Column(nullable = false)
    private String refund; // 환불 규정

    @Lob
    @Column(nullable = false)
    private String guide; // 시설안내

    @Lob
    @Column(nullable = false)
    private String address; // 주소 이용한 지도




    public void updateProduct(ProductFormDto productFormDto){
        this.name=productFormDto.getName();
        this.price=productFormDto.getPrice();
        this.fit=productFormDto.getFit();
        this.description= productFormDto.getDescription();
        this.productStatus=productFormDto.getProductStatus();
        this.useTime=productFormDto.getUseTime();
        this.refund=productFormDto.getRefund();
        this.guide=productFormDto.getGuide();
        this.address=productFormDto.getAddress();
    }

    // Product 엔티티가 삭제될 때 해당 Product와 매핑되어 있는 모든 ProductImage 엔티티도 함께 삭제됩니다.
    // 또한, orphanRemoval = true 옵션을 추가하면 ProductImage 엔티티가 Product 엔티티와의 연관관계에서 해제될 때 해당 ProductImage 엔티티도 자동으로 삭제됩니다.

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    // 상품 주문시 이용가능 시간 감소
    public void removeStock(int vuseTime){
        int restTime = this.useTime - vuseTime ;
        if(restTime < 0){ // 재고 부족
            String message = "이용 시간이 부족합니다." + this.useTime;
            throw new OutOfStockException(message);
        }
        this.useTime = restTime ;
    }

    public void addUseTime(int vuseTime) {
        this.useTime += vuseTime;
    }
}
