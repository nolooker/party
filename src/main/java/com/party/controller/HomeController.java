package com.party.controller;

import com.party.dto.*;
import com.party.repository.MemberRepository;
import com.party.service.ProductService;
import com.party.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final ProductService productService;
    private final ReviewService reviewService;

    @RequestMapping(value = "/")
    public String main(HttpSession session, ProductSearchDto dto, ReviewSearchDto dtoo, Optional<Integer> page, Model model) {
        String sessionId = "User";
        if (session.getAttribute(sessionId) == null) { // 세션 영역에 데이터가 없을 때만 세션에 바인딩 합니다.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) { // 유저 정보가 null일 때 대비
                Object principal = authentication.getPrincipal();

                if (principal instanceof User) {
                    User user = (User) principal;
                    String email = user.getUsername(); // 로그인한 유저의 id 값을 가져옵니다.


                    MemberDto result = memberRepository.getMemberInfo(email);
                    // 유저 정보를 데이터베이스와 연동하여 필요한 값만 가져옵니다.

                    if (result != null) {
                        System.out.println("세션 영역에 데이터를 바인딩합니다.");
                        session.setAttribute(sessionId, result);
                    }
                }
            }
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 100);

        if (dto.getSearchQuery() == null) {
            dto.setSearchQuery("");
        }

        Page<MainProductDto> products = productService.getMainProductPage(dto, pageable);
        Page<MainReviewDto> reviews = reviewService.getMainReviewPage(dtoo, pageable);

        model.addAttribute("products", products);
        model.addAttribute("reviews",reviews);


        return "main";

    }

}
