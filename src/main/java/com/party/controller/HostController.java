package com.party.controller;

import com.party.dto.HostProductDto;
import com.party.dto.ProductFormDto;
import com.party.dto.ProductImageDto;
import com.party.dto.ProductSearchDto;
import com.party.entity.Reserve;
import com.party.service.MemberService;
import com.party.service.ProductImageService;
import com.party.service.ProductService;
import com.party.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HostController {

    private final ProductService productService;
    private final MemberService memberService;
    private final ProductImageService productImageService;
    private final ReserveService reserveService ;


    @GetMapping(value = {"/host/products", "/host/products/{page}"})
    public String productManage(ProductSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model, Authentication authentication) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String loggedInUsername = userDetails.getUsername();

            // 사용자 이름을 기준으로 상품을 조회하도록 설정
            dto.setCreatedBy(loggedInUsername);
        }

        Page<HostProductDto> products = productService.getHostProductPage(dto, pageable);

        model.addAttribute("products", products);
        model.addAttribute("searchDto", dto); // 검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5); // 하단에 보여줄 최대 페이지 번호

        return "product/prList";
    }


    //상품 업데이트 코딩
    @PostMapping(value = "/host/product/{productId}")
    public String productUpdate(@Valid ProductFormDto dto, BindingResult error, Model model,
                                @RequestParam("productImageFile") List<MultipartFile> uploadedFile,
                                @PathVariable("productId") Long productId) {

        String whenError = "/product/prUpdateForm";
        ProductFormDto ddto = productService.getProductDetail(productId);
        model.addAttribute("productFormDto", ddto);

        if (error.hasErrors()) {
            List<ProductImageDto> productImageDtoList = new ArrayList<>();

            for (Long id : dto.getProductImageIds()) {
                ProductImageDto imageDto = productImageService.getProductImage(id);
                productImageDtoList.add(imageDto);
            }

            dto.setProductImageDtoList(productImageDtoList);

            model.addAttribute("productFormDto", dto);
            return whenError;
        }

        if (uploadedFile.get(0).isEmpty() && dto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 이미지는 필수 입력 사항입니다.");
            return whenError;
        }

        try {
            productService.updateProduct(dto, uploadedFile);

        } catch (Exception err) {
            model.addAttribute("errorMessage", "상품 수정 중에 오류가 발생하였습니다.");
            err.printStackTrace();
            return whenError;
        }

        return "redirect:/"; // 메인 페이지로 이동
    }

    //상품 삭제 코딩
    @PostMapping("/host/product/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/";
    }



    @GetMapping(value = {"/reservelist", "/reservelist/{page}"})
    public String productreservelist(ProductSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model, Authentication authentication) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String loggedInUsername = userDetails.getUsername();


            dto.setCreatedBy(loggedInUsername);


            // 예약 목록과 회원 정보 가져오기
            List<Reserve> reservationList = reserveService.findAllReservationsWithMembers();

            // 모델에 예약 목록과 회원 정보 전달
            model.addAttribute("reservations", reservationList);
        }

        Page<HostProductDto> products = productService.getHostProductPage(dto, pageable);
        model.addAttribute("searchDto", dto);
        model.addAttribute("maxPage", 5);

        return "reserve/reserveList";
    }
}

