package com.party.service;

import com.party.dto.*;
import com.party.entity.Product;
import com.party.entity.ProductImage;
import com.party.repository.ProductImageRepository;
import com.party.repository.ProductRepository;
import com.party.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
	private final ProductImageRepository productImageRepository;
    private final ReserveRepository reserveRepository;

    // 상품 등록
    public Long saveProduct(ProductFormDto dto, List<MultipartFile> uploadedFile) throws Exception {
        Product product = dto.createProduct();
        productRepository.save(product);

        //상품에 들어가는 각 이미지들
        for (int i = 0; i < uploadedFile.size(); i++) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);

            if (i == 0) {
                productImage.setRepImageYesNo("Y");
            } else {
                productImage.setRepImageYesNo("No");
            }
            productImageService.saveProductImage(productImage, uploadedFile.get(i));
        }

        return product.getId().longValue();
    }


    //등록된 상품 정보를 읽어 들입니다.
    public ProductFormDto getProductDetail(Long productId){
        List<ProductImage> productImageList =productImageRepository.findByProductIdOrderByIdAsc(productId);

        List<ProductImageDto> productImageDtoList= new ArrayList<ProductImageDto>();

        for (ProductImage productImg : productImageList){
            ProductImageDto productImgDto = ProductImageDto.of(productImg);
            productImageDtoList.add(productImgDto);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        ProductFormDto dto = ProductFormDto.of(product);

        dto.setProductImageDtoList(productImageDtoList);

        return dto;
    }
    public Long updateProduct(ProductFormDto dto,List<MultipartFile> uploadedFile) throws Exception{
        Product product=productRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        //화면에서 넘어온 데이터를 Entity에게 전달합니다.
        product.updateProduct(dto);

        //5개의 이미지들에 대한 목록
        List<Long> productImageIds=dto.getProductImageIds();

        for (int i = 0; i <uploadedFile.size(); i++) {
            productImageService.updateProductImage(productImageIds.get(i),uploadedFile.get(i));

        }
        return product.getId();

    }
    public Page<Product> getAdminProductPage(ProductSearchDto dto, Pageable pageable){
        // 상품 검색 조건 dto와 페이징 객체 pageable을 사용해서 페이징 객체를 구해줍니다.
        return productRepository.getAdminProductPage(dto,pageable);
    }
    public Page<MainProductDto> getMainProductPage(ProductSearchDto dto, Pageable pageable){
        return productRepository.getMainProductPage(dto,pageable);
    }

    //  해당 상품을 삭제합니다.
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
	
	//호스트 상품목록 리스트
    public Page<HostProductDto> getHostProductPage(ProductSearchDto dto, Pageable pageable){
       return productRepository.getHostProductPage(dto,pageable);
    }



}
