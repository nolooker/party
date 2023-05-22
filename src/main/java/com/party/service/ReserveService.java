package com.party.service;

import com.party.dto.ReserveDto;
import com.party.dto.ReserveHistDto;
import com.party.dto.ReserveProductDto;
import com.party.entity.*;
import com.party.repository.MemberRepository;
import com.party.repository.ReserveRepository;
import com.party.repository.ProductImageRepository;
import com.party.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReserveService {
    private final ProductRepository productRepository ;
    private final MemberRepository memberRepository ;
    private final ReserveRepository reserveRepository ;

    // email 정보와 주문 정보(ReserveDto)를 이용하여 주문 로직을 구합니다.
    public Long reserve(ReserveDto reserveDto, String email){
        // 어떤 상품인가요?
        Product product = productRepository.findById(reserveDto.getProductId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email) ;
        List<ReserveProduct> reserveProductList = new ArrayList<>() ;
        ReserveProduct reserveProduct = ReserveProduct.createReserveProduct(product, reserveDto.getCount(),reserveDto.getStartTimeAsLocalDateTime(), reserveDto.getEndTimeAsLocalDateTime(), reserveDto.getPersonnel(), reserveDto.getReq());
        reserveProductList.add(reserveProduct);

        LocalDateTime startTime = reserveDto.getStartTimeAsLocalDateTime();
        LocalDateTime endTime = startTime.plusHours(reserveDto.getCount());

        Integer personnel = reserveDto.getPersonnel();
        String req = reserveDto.getReq();

        Reserve reserve = Reserve.createreserve(member, reserveProductList,startTime,endTime, personnel, req) ;
        reserve.setMember(member);
        reserve.setReserveProducts(reserveProductList);
        reserve.setStartTime(startTime);
        reserve.setEndTime(endTime);
        reserve.setPersonnel(personnel);
        reserve.setReq(req);

        reserveRepository.save(reserve) ;
        return reserve.getId();
    }




    private final ProductImageRepository productImageRepository ;

    public Page<ReserveHistDto> getReserveList(String email, Pageable pageable){
        List<Reserve> reserves = reserveRepository.findReserves(email, pageable) ;
        Long totalCount = reserveRepository.countReserve(email) ;

        List<ReserveHistDto> reserveHistDtos = new ArrayList<>() ;

        for(Reserve reserve : reserves){ // 주문 목록 반복
            ReserveHistDto reserveHistDto = new ReserveHistDto(reserve);
            List<ReserveProduct> reserveProducts = reserve.getReserveProducts() ;

            for(ReserveProduct bean : reserveProducts){
                ProductImage productImage = productImageRepository.findByProductIdAndRepImageYesNo(bean.getProduct().getId(), "Y") ;

                ReserveProductDto beanDto = new ReserveProductDto(bean, productImage.getImageUrl()) ;

                reserveHistDto.addReserveProductDto(beanDto);
            }

            reserveHistDtos.add(reserveHistDto) ;
        }

        return new PageImpl<ReserveHistDto>(reserveHistDtos, pageable, totalCount);
    }

    public void cancelReserve(Long reserveId){
        Reserve reserve = reserveRepository.findById(reserveId)
                .orElseThrow(EntityNotFoundException::new) ;
        reserve.cancelReserve();
    }

    // 로그인 한 사람과 이메일의 주소가 동일한지 검사합니다.
    public boolean validateReserve(Long reserveId, String email){
        Member curMember = memberRepository.findByEmail(email) ; // 로그인 한 사람
        Reserve reserve = reserveRepository.findById(reserveId)
                .orElseThrow(EntityNotFoundException::new) ;
        Member savedMember = reserve.getMember() ; // 주문한 사람

        if(StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return true ;
        }else{
            return false ;
        }
    }
    // 장바구니에서 주문할 상품 데이터를 전달 받아서 주문을 생성하는 로직을 구현합니다.
    public Long reserves(List<ReserveDto> reserveDtoList, String email, LocalDateTime startTime, LocalDateTime endTime, Integer personnel, String req){
        // reserveDtoList : 상품 아이디와 수량을 가지고 있는 객체들의 모음
        Member member = memberRepository.findByEmail(email) ;
        List<ReserveProduct> reserveProductList = new ArrayList<>() ; // 주문할 상품 리스트
        for(ReserveDto dto : reserveDtoList){
            Long productId = dto.getProductId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(EntityNotFoundException::new);
            int count = dto.getCount();

            ReserveProduct reserveProduct = ReserveProduct.createReserveProduct(product, count,startTime, endTime, personnel, req);
            reserveProductList.add(reserveProduct) ;
        }

        Reserve reserve = Reserve.createreserve(member, reserveProductList,startTime,endTime,personnel,req);
        reserve.setMember(member);
        reserve.setReserveProducts(reserveProductList);
        reserve.setStartTime(startTime);
        reserve.setEndTime(endTime);
        reserve.setPersonnel(personnel);
        reserve.setReq(req);
        reserveRepository.save(reserve) ;

        return reserve.getId();
    }
}
