package com.party.controller;

import com.party.dto.*;
import com.party.service.ReviewImageService;
import com.party.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 이용후기 등록 폼
    @GetMapping(value = "/review/new")
    public String reviewForm(Model model) {
        model.addAttribute("reviewFormDto", new ReviewFormDto());

        return "/review/rwInsertForm";
    }

    // 이용후기 등록 중
    @PostMapping(value = "/review/new")
    public String reviewNew(@Valid ReviewFormDto dto, BindingResult error, Model model,
                             @RequestParam("reviewImageFile") List<MultipartFile> uploadedFile) {
        if (error.hasErrors()) {
            return "/review/rwInsertForm";
        }
        if (uploadedFile.get(0).isEmpty() && dto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 이미지는 필수 입력 값입니다.");
            return "/review/rwInsertForm";
        }

        try {
            reviewService.saveReview(dto, uploadedFile);


        } catch (Exception err) {
            err.printStackTrace();
            model.addAttribute("errorMessage", "예외가 발생했습니다.");
            return "/review/rwInsertForm";

        }
        return "redirect:/"; //메인페이지로 이동
    }

    // 이용후기 수정
    @GetMapping(value = "/my/review/{reviewId}")
    public String reviewDetail(@PathVariable Long reviewId, Model model) {
        try {
            ReviewFormDto dto = reviewService.getReviewDetail(reviewId);
            model.addAttribute("reviewFormDto", dto);
        } catch (EntityNotFoundException err) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("reviewFormDto", new ReviewFormDto());
        }
        return "/review/rwUpdateForm";
    }


    private final ReviewImageService reviewImageService;

    @PostMapping(value = "/my/review/{reviewId}")
    public String reviewUpdate(@Valid ReviewFormDto dto, BindingResult error, Model model,
                                @RequestParam("reviewImageFile") List<MultipartFile> uploadedFile,
                                @PathVariable("reviewId") Long reviewId) {

        String whenError = "/review/rwUpdateForm";
        ReviewFormDto ddto = reviewService.getReviewDetail(reviewId);
        model.addAttribute("reviewFormDto", ddto);

        if (error.hasErrors()) {
            List<ReviewImageDto> reviewImageDtoList = new ArrayList<>();

            for (Long id : dto.getReviewImageIds()) {
                ReviewImageDto imageDto = reviewImageService.getReviewImage(id);
                reviewImageDtoList.add(imageDto);
            }

            dto.setReviewImageDtoList(reviewImageDtoList);

            model.addAttribute("reviewFormDto", dto);
            return whenError;
        }

        if (uploadedFile.get(0).isEmpty() && dto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 이미지는 필수 입력 사항입니다.");
            return whenError;
        }

        try {
            reviewService.updateReview(dto, uploadedFile);

        } catch (Exception err) {
            model.addAttribute("errorMessage", "이용후기 수정 중에 오류가 발생하였습니다.");
            err.printStackTrace();
            return whenError;
        }

        return "redirect:/"; // 메인 페이지로 이동
    }

    // 이용 후기 목록(본인)
    @GetMapping(value = {"my/review", "/review/{page}"})
    public String reviewManage(ReviewSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6); // 한 페이지당 6개씩 표시
        Page<HostReviewDto> reviews = reviewService.getHostReviewPage(dto, pageable);

        model.addAttribute("reviews", reviews);
        model.addAttribute("searchDto", dto); // 검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5); // 하단에 보여줄 최대 페이지 번호

        return "review/rwList";
    }

    // 이용후기 목록
    @GetMapping(value = {"/list/reviews", "/list/reviews/{page}"})
    public String reviewList(ReviewSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6); // 한 페이지당 6개씩 표시
        Page<MainReviewDto> reviews = reviewService.getMainReviewPage(dto, pageable);

        model.addAttribute("reviews", reviews);
        model.addAttribute("searchDto", dto); // 검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5); // 하단에 보여줄 최대 페이지 번호

        return "review/rwList2";
    }

    @GetMapping(value = "/review/detail/{reviewId}")
    // 일반 사용자가 상품을 클릭하여 상세 페이지로 이동
    public String reviewDetail(Model model, @PathVariable("reviewId") Long reviewId) {
        ReviewFormDto dto = reviewService.getReviewDetail(reviewId);
        model.addAttribute("review", dto);
        return "review/rwDetail";

    }

//    @GetMapping(value = "/review/{reviewId}")
//    // 일반 사용자가 상품을 클릭하여 상세 페이지로 이동
//    public String reviewDetail(Model model, @PathVariable("reviewId") Long reviewId) {
//        ReviewFormDto dto = reviewService.getReviewDetail(reviewId);
//        model.addAttribute("review", dto);
//        return "review/rwDetail";
//    }

    @PostMapping("/my/review/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/";
    }
}