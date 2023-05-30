package com.party.repository;


import com.party.dto.*;
import com.party.entity.QReview;
import com.party.entity.QReviewImage;
import com.party.entity.Review;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{
    private JPAQueryFactory queryFactory;
	private HttpServletRequest request;

    @Override
    public Page<MainReviewDto> getMainReviewPage(ReviewSearchDto searchDto, Pageable pageable) {
        QReview review=QReview.review;
        QReviewImage reviewImage=QReviewImage.reviewImage;
        QueryResults<MainReviewDto> results=this.queryFactory
                .select(
                        new QMainReviewDto(
                                review.id,
                                review.name,
                                review.title,
                                review.description,
                                reviewImage.imageUrl,
                                review.rating


                        )
                )
                .from(reviewImage)
                .join(reviewImage.review,review)
                .where(reviewImage.repImageYesNo.eq("Y"))
                .where(searchByCondition(searchDto.getSearchBy(),searchDto.getSearchQuery()))
                .orderBy(QReview.review.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainReviewDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

	//본인 상품 리스트
    @Override
    public Page<HostReviewDto> getHostReviewPage(ReviewSearchDto searchDto, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) { // 유저 정보가 null일 때 대비
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                User user = (User) principal;
                String sessionId = user.getUsername(); // 로그인한 유저의 id 값을 가져옵니다.

                QReview review = QReview.review;
                QReviewImage reviewImage = QReviewImage.reviewImage;
                QueryResults<HostReviewDto> results = this.queryFactory
                        .select(
                                new QHostReviewDto(
                                        review.id,
                                        review.name,
                                        review.title,
                                        review.description,
                                        reviewImage.imageUrl,
                                        review.regdate,
                                        review.createBy,
                                        review.rating

                                )
                        )
                        .from(reviewImage)
                        .join(reviewImage.review, review)
                        .where(reviewImage.repImageYesNo.eq("Y"))
                        .where(review.createBy.eq(sessionId)) // 로그인한 사용자의 create_by 컬럼과 일치하는지 확인
                        .where(searchByCondition(searchDto.getSearchBy(),searchDto.getSearchQuery()))
                        .orderBy(QReview.review.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

                List<HostReviewDto> content = results.getResults();
                long total = results.getTotal();

                return new PageImpl<>(content, pageable, total);
            }
        }

        // 로그인하지 않은 경우 빈 페이지를 반환하거나 예외 처리 등을 수행할 수 있습니다.
        return new PageImpl<>(Collections.emptyList());
    }


    private BooleanExpression likeCondition(String searchQuery) {
        // 검색 키워드가 null이 아니면 like 연산을 수행합니다.

        return StringUtils.isEmpty(searchQuery) ? null: QReview.review.name.like("%"+searchQuery+"%");
    }

    @Override
    public Page<Review> getAdminReviewPage(ReviewSearchDto searchDto, Pageable pageable) {

        QueryResults<Review> results = this.queryFactory
                .selectFrom(QReview.review)
                .orderBy(QReview.review.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Review> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }



    public ReviewRepositoryCustomImpl(EntityManager em) {
        this.queryFactory =new JPAQueryFactory(em);
    }


    private BooleanExpression searchByCondition(String searchBy, String searchQuery) {
        if(StringUtils.equals("name",searchBy)){ //상품 이름으로 검색
            return QReview.review.name.like("%"+searchQuery+"%");
        }else if (StringUtils.equals("createBy",searchBy)){ // 상품 등록자로 검색
            return QReview.review.createBy.like("%"+searchQuery+"%");

        }
        return null;
    }
//    private BooleanExpression sellStatusCondition(ReviewStatus reviewStatus) {
//        return reviewStatus==null? null:QReview.review.reviewStatus.eq(reviewStatus);
//    }

}
