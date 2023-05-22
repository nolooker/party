package com.party.repository;

import com.party.constant.ProductStatus;
import com.party.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;


    @Test
    @DisplayName("상품 저장 테스트")
    public void createProductTest() {
        Product product = new Product();
        product.setProductStatus(ProductStatus.NOT_RESERVE);
        //product.setId();
        product.setDescription("놀기 좋은 방이에요");
        product.setName("놀기좋은방");
        product.setPrice(100000);
        product.setFit("5");
        product.setRegdate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());

        Product saveItem = productRepository.save(product);
        System.out.println(saveItem.toString());

    }
}
