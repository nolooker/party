package com.party.service;

import com.party.dto.ReviewImageDto;
import com.party.entity.ReviewImage;
import com.party.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImageService {
    @Value("${reviewImageLocation}")
    private String reviewImageLocation; // 상품 이미지가 업로드되는 경로

    private final ReviewImageRepository reviewImageRepository;
    private final FileService fileService;

    // 상품 이미지 정보 저장
    public void saveReviewImage(ReviewImage reviewImage, MultipartFile uploadedFile) throws Exception {
        String oriImageName = uploadedFile.getOriginalFilename();
        String imageName = "";
        String imageURl = "";
        System.out.println("reviewImageLocation:" + reviewImageLocation);

        //파일 업로드
        if (!StringUtils.isEmpty(oriImageName)) { // 이름이 있으면 업로드 하자
            imageName = fileService.uploadFile(reviewImageLocation, oriImageName, uploadedFile.getBytes());
            imageURl = "/images/" + imageName;
            System.out.println("imageUrl:" + imageURl);
        }

        reviewImage.updateReviewImage(oriImageName, imageName, imageURl);
        reviewImageRepository.save(reviewImage);

    }

    public void updateReviewImage(Long reviewImageId, MultipartFile uploadedFile) throws Exception {
        if (!uploadedFile.isEmpty()) { //업로드할 이미지가 있으면
            ReviewImage previousImage = reviewImageRepository.findById(reviewImageId)
                    .orElseThrow(EntityNotFoundException::new);

            if (!StringUtils.isEmpty(previousImage.getImageName())) {
                fileService.deleteFile(reviewImageLocation + "/" + previousImage.getImageName());
            }

            String oriImageName = uploadedFile.getOriginalFilename();
            String imageName = fileService.uploadFile(reviewImageLocation, oriImageName, uploadedFile.getBytes());

            String imageURl = "/images/" + imageName;

            previousImage.updateReviewImage(oriImageName, imageName, imageURl);
        }

    }

    public ReviewImageDto getReviewImage(Long id) {
        ReviewImageDto dto = ReviewImageDto.of(reviewImageRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        return dto ;
    }
}
