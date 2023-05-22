package com.party.controller;

import com.party.dto.MainProductDto;
import com.party.dto.ProductSearchDto;
import com.party.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {




    private final ProductService productService ;

    @PostMapping(value = "/")
    public String main(ProductSearchDto dto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get(): 0,3) ;

        if(dto.getSearchQuery() == null) {
            dto.setSearchQuery("");
        }

        Page<MainProductDto> products = productService.getMainProductPage(dto, pageable);

        model.addAttribute("products", products) ;
        model.addAttribute("searchDto", dto) ;
        model.addAttribute("maxPage", 5) ;

        return "index" ;

    }
}


