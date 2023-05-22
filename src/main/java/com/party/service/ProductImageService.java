package com.party.service;

import com.party.dto.ProductImageDto;
import com.party.entity.ProductImage;
import com.party.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {
    @Value("${productImageLocation}")
    private String productImageLocation; // 상품 이미지가 업로드되는 경로

    private final ProductImageRepository productImageRepository;
    private final FileService fileService;

    // 상품 이미지 정보 저장
    public void saveProductImage(ProductImage productImage, MultipartFile uploadedFile) throws Exception {
        String oriImageName = uploadedFile.getOriginalFilename();
        String imageName = "";
        String imageURl = "";
        System.out.println("productImageLocation:" + productImageLocation);

        //파일 업로드
        if (!StringUtils.isEmpty(oriImageName)) { // 이름이 있으면 업로드 하자
            imageName = fileService.uploadFile(productImageLocation, oriImageName, uploadedFile.getBytes());
            imageURl = "/images/" + imageName;
            System.out.println("imageUrl:" + imageURl);
        }

        productImage.updateProductImage(oriImageName, imageName, imageURl);
        productImageRepository.save(productImage);

    }

    public void updateProductImage(Long productImageId, MultipartFile uploadedFile) throws Exception {
        if (!uploadedFile.isEmpty()) { //업로드할 이미지가 있으면
            ProductImage previousImage = productImageRepository.findById(productImageId)
                    .orElseThrow(EntityNotFoundException::new);

            if (!StringUtils.isEmpty(previousImage.getImageName())) {
                fileService.deleteFile(productImageLocation + "/" + previousImage.getImageName());
            }

            String oriImageName = uploadedFile.getOriginalFilename();
            String imageName = fileService.uploadFile(productImageLocation, oriImageName, uploadedFile.getBytes());

            String imageURl = "/images/" + imageName;

            previousImage.updateProductImage(oriImageName, imageName, imageURl);
        }

    }

    public ProductImageDto getProductImage(Long id) {
        ProductImageDto dto = ProductImageDto.of(productImageRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        return dto ;
    }
}
