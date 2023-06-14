package com.party.service;

import com.party.dto.*;
import com.party.entity.Review;
import com.party.entity.ReviewImage;
import com.party.repository.ReviewImageRepository;
import com.party.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageService reviewImageService;
	private final ReviewImageRepository reviewImageRepository;

    // 이용 후기 등록
    public Long saveReview(ReviewFormDto dto, List<MultipartFile> uploadedFile, String rating) throws Exception {
        Review review = dto.createReview();
        review.setRating(rating); // 별점 설정
        reviewRepository.save(review);

        // 이용 후기에 들어가는 각 이미지들
        for (int i = 0; i < uploadedFile.size(); i++) {
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setReview(review);

            if (i == 0) {
                reviewImage.setRepImageYesNo("Y");
            } else {
                reviewImage.setRepImageYesNo("No");
            }
            reviewImageService.saveReviewImage(reviewImage, uploadedFile.get(i));
        }

        return review.getId().longValue();
    }


    //등록된 후기 정보를 읽어 들입니다.
    public ReviewFormDto getReviewDetail(Long reviewId){
        List<ReviewImage> reviewImageList =reviewImageRepository.findByReviewIdOrderByIdAsc(reviewId);

        List<ReviewImageDto> reviewImageDtoList= new ArrayList<ReviewImageDto>();

        for (ReviewImage reviewImg : reviewImageList){
            ReviewImageDto reviewImgDto = ReviewImageDto.of(reviewImg);
            reviewImageDtoList.add(reviewImgDto);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(EntityNotFoundException::new);

        ReviewFormDto dto = ReviewFormDto.of(review);

        dto.setReviewImageDtoList(reviewImageDtoList);

        return dto;
    }


    public Long updateReview(ReviewFormDto dto,List<MultipartFile> uploadedFile) throws Exception{
        Review review=reviewRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        //화면에서 넘어온 데이터를 Entity에게 전달합니다.
        review.updateReview(dto);

        //5개의 이미지들에 대한 목록
        List<Long> reviewImageIds=dto.getReviewImageIds();

        for (int i = 0; i <uploadedFile.size(); i++) {
            reviewImageService.updateReviewImage(reviewImageIds.get(i),uploadedFile.get(i));

        }
        return review.getId();

    }
    public Page<Review> getAdminReviewPage(ReviewSearchDto dto, Pageable pageable){
        // 후기 검색 조건 dto와 페이징 객체 pageable을 사용해서 페이징 객체를 구해줍니다.
        return reviewRepository.getAdminReviewPage(dto,pageable);
    }
    public Page<MainReviewDto> getMainReviewPage(ReviewSearchDto dto, Pageable pageable){
        return reviewRepository.getMainReviewPage(dto,pageable);
    }

    //  해당 상품을 삭제합니다.
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
	
	//본인 후기목록 리스트
    public Page<HostReviewDto> getHostReviewPage(ReviewSearchDto dto, Pageable pageable){
       return reviewRepository.getHostReviewPage(dto,pageable);
    }

}
