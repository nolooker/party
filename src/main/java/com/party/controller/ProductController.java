package com.party.controller;

import com.party.dto.MainProductDto;
import com.party.dto.ProductFormDto;
import com.party.dto.ProductImageDto;
import com.party.dto.ProductSearchDto;
import com.party.service.ProductImageService;
import com.party.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    @GetMapping(value = "/admin/products/new")
    public String productForm(Model model) {
        model.addAttribute("productFormDto", new ProductFormDto());

        return "/product/prInsertForm";
    }

    private final ProductService productService;

    @PostMapping(value = "/admin/products/new")
    public String productNew(@Valid ProductFormDto dto, BindingResult error, Model model,
                             @RequestParam("productImageFile") List<MultipartFile> uploadedFile) {
        if (error.hasErrors()) {
            return "/product/prInsertForm";
        }
        if (uploadedFile.get(0).isEmpty() && dto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 이미지는 필수 입력 값입니다.");
            return "/product/prInsertForm";
        }

        try {
            productService.saveProduct(dto, uploadedFile);


        } catch (Exception err) {
            err.printStackTrace();
            model.addAttribute("errorMessage", "예외가 발생했습니다.");
            return "/product/prInsertForm";

        }
        return "redirect:/"; //메인페이지로 이동
    }

    @GetMapping(value = "/admin/product/{productId}")
    public String productDetail(@PathVariable Long productId, Model model) {
        try {
            ProductFormDto dto = productService.getProductDetail(productId);
            model.addAttribute("productFormDto", dto);
        } catch (EntityNotFoundException err) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("productFormDto", new ProductFormDto());
        }
        return "/product/prUpdateForm";
    }


    private final ProductImageService productImageService;

    @PostMapping(value = "/admin/product/{productId}")
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

    @GetMapping(value = {"/admin/products", "/admin/products/{page}"})
    public String productMange(ProductSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<MainProductDto> products = productService.getMainProductPage(dto, pageable);

        model.addAttribute("products", products);
        model.addAttribute("searchDto", dto); // 검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5); // 하단에 보여줄 최대 페이지 번호

        return "product/prList";
    }

    @GetMapping(value = {"/list/products", "/list/products/{page}"})
    public String productList(ProductSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<MainProductDto> products = productService.getMainProductPage(dto, pageable);

        model.addAttribute("products", products);
        model.addAttribute("searchDto", dto); // 검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5); // 하단에 보여줄 최대 페이지 번호

        return "product/prList2";
    }

    @GetMapping(value = "/product/{productId}")
    // 일반 사용자가 상품을 클릭하여 상세 페이지로 이동
    public String productDetail(Model model, @PathVariable("productId") Long productId) {

        ProductFormDto dto = productService.getProductDetail(productId);

        model.addAttribute("product", dto);


        return "product/prDetail";

    }

    @PostMapping("/admin/product/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }
}