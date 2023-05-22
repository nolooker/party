package com.party.repository;

import com.party.entity.ReserveProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveProductRepository extends JpaRepository<ReserveProduct, Long> {

    // 필요 없을 수도~
}
