package com.party.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review_room_image")
@Getter @Setter
public class ReviewImage extends BaseEntity {

    @Id
    @Column(name = "review_room_image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageName; //이미지 이름

    private String oriImageName; // 원본 이미지 이름

    private String imageUrl; // 이미지 조회 경로

    private String repImageYesNo; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_room_id")
    private Review review;

    public void updateReviewImage(String oriImageName,String imageName,String imageUrl){
        this.oriImageName = oriImageName;
        this.imageName=imageName;
        this.imageUrl = imageUrl;
    }


}
