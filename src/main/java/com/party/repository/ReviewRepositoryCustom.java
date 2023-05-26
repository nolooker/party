package com.party.repository;

import com.party.dto.HostReviewDto;
import com.party.dto.MainReviewDto;
import com.party.dto.ReviewSearchDto;
import com.party.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Page<Review> getAdminReviewPage(ReviewSearchDto searchDto, Pageable pageable);

    Page<MainReviewDto> getMainReviewPage(ReviewSearchDto searchDto, Pageable pageable);
    
	Page<HostReviewDto> getHostReviewPage(ReviewSearchDto searchDto, Pageable pageable);
}



