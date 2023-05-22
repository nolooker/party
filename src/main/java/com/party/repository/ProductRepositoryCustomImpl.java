package com.party.repository;

import com.party.constant.ProductStatus;
import com.party.dto.*;
import com.party.entity.Product;
import com.party.entity.QProduct;
import com.party.entity.QProductImage;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    private JPAQueryFactory queryFactory;
	private HttpServletRequest request;

    @Override
    public Page<MainProductDto> getMainProductPage(ProductSearchDto searchDto, Pageable pageable) {
        QProduct product=QProduct.product;
        QProductImage productImage=QProductImage.productImage;
        QueryResults<MainProductDto> results=this.queryFactory
                .select(
                        new QMainProductDto(
                                product.id,
                                product.name,
                                product.description,
                                product.fit,
                                product.useTime,
                                productImage.imageUrl,
                                product.price,
                                product.productStatus
                        )
                )
                .from(productImage)
                .join(productImage.product,product)
                .where(productImage.repImageYesNo.eq("Y"))
                .where(sellStatusCondition(searchDto.getProductStatus()),
                        searchByCondition(searchDto.getSearchBy(),searchDto.getSearchQuery()))
                .orderBy(QProduct.product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainProductDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }
	
	//호스트 본인 상품 리스트
    @Override
    public Page<HostProductDto> getHostProductPage(ProductSearchDto searchDto, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) { // 유저 정보가 null일 때 대비
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                User user = (User) principal;
                String sessionId = user.getUsername(); // 로그인한 유저의 id 값을 가져옵니다.

                QProduct product = QProduct.product;
                QProductImage productImage = QProductImage.productImage;
                QueryResults<HostProductDto> results = this.queryFactory
                        .select(
                                new QHostProductDto(
                                        product.id,
                                        product.name,
                                        product.description,
                                        product.fit,
                                        product.useTime,
                                        productImage.imageUrl,
                                        product.price,
                                        product.productStatus
                                )
                        )
                        .from(productImage)
                        .join(productImage.product, product)
                        .where(productImage.repImageYesNo.eq("Y"))
                        .where(sellStatusCondition(searchDto.getProductStatus()),
                                searchByCondition(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                        .where(product.createBy.eq(sessionId)) // 로그인한 사용자의 create_by 컬럼과 일치하는지 확인
                        .orderBy(QProduct.product.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

                List<HostProductDto> content = results.getResults();
                long total = results.getTotal();

                return new PageImpl<>(content, pageable, total);
            }
        }

        // 로그인하지 않은 경우 빈 페이지를 반환하거나 예외 처리 등을 수행할 수 있습니다.
        return new PageImpl<>(Collections.emptyList());
    }


    private BooleanExpression likeCondition(String searchQuery) {
        // 검색 키워드가 null이 아니면 like 연산을 수행합니다.

        return StringUtils.isEmpty(searchQuery) ? null: QProduct.product.name.like("%"+searchQuery+"%");
    }

    @Override
    public Page<Product> getAdminProductPage(ProductSearchDto searchDto, Pageable pageable) {

        QueryResults<Product> results = this.queryFactory
                .selectFrom(QProduct.product)
                .where(sellStatusCondition(searchDto.getProductStatus()),
                        searchByCondition(searchDto.getSearchBy(),searchDto.getSearchQuery()))
                .orderBy(QProduct.product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Product> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }



    public ProductRepositoryCustomImpl(EntityManager em) {
        this.queryFactory =new JPAQueryFactory(em);
    }


    private BooleanExpression searchByCondition(String searchBy, String searchQuery) {
        if(StringUtils.equals("name",searchBy)){ //상품 이름으로 검색
            return QProduct.product.name.like("%"+searchQuery+"%");
        }else if (StringUtils.equals("createBy",searchBy)){ // 상품 등록자로 검색
            return QProduct.product.createBy.like("%"+searchQuery+"%");

        }
        return null;
    }
    private BooleanExpression sellStatusCondition(ProductStatus productStatus) {
        return productStatus==null? null:QProduct.product.productStatus.eq(productStatus);
    }

}
