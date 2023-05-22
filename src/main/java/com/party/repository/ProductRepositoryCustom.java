package com.party.repository;

import com.party.dto.HostProductDto;
import com.party.dto.MainProductDto;
import com.party.dto.ProductSearchDto;
import com.party.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> getAdminProductPage(ProductSearchDto searchDto, Pageable pageable);

    Page<MainProductDto> getMainProductPage(ProductSearchDto searchDto, Pageable pageable);
    
	Page<HostProductDto> getHostProductPage(ProductSearchDto dto, Pageable pageable);
}



