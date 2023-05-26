package com.party.repository;

import com.party.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReviewIdOrderByIdAsc(Long reviewId);

    ReviewImage findByReviewIdAndRepImageYesNo(Long id, String repImageYesNo);


}
