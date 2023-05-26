package com.party.repository;

import com.party.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, QuerydslPredicateExecutor<Review>, ReviewRepositoryCustom {

    //쿼리 메소드 작성
    // 상품의 이름으로 조회하여 목록을 반환받기
    List<Review> findReviewByName(String name);

//	List<Review> findReviewByFit(String fit);

    //특정 가격 이하의 데이터만 조회
//    List<Review> findByPriceLessThan(Integer price);

    //특정 가격 이하의 데이터를 조회하되, 가격에 대하여 내림차순 정렬
//    List<Review> findByPriceLessThanOrderByPriceDesc(Integer price);

    void deleteById(Long id);

    //엔터티명으로  price <=>  name
    @Query("select i from Review i where i.description like "+
            "%:description% order by i.name desc ")
    List<Review> findByReviewDetail01(@Param("description") String description);

    //테이블명으로 명시
    @Query(value = "select * from Reviews i where i.description like "+
            "%:description% order by i.name desc ",nativeQuery = true)
    List<Review> findByReviewDetail02(@Param("description") String description);
}

