package com.party.repository;

import com.party.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdOrderByIdAsc(Long productId);

    ProductImage findByProductIdAndRepImageYesNo(Long id, String repImageYesNo);


}
