package com.party.repository;

import com.party.dto.MemberDto;
import com.party.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    // 이메일을 이용하여 회원 정보 조회
    Member findByEmail(String email);


    Member findById(Long id);

    @Query("select new com.party.dto.MemberDto(m.id, m.name, m.email, m.address, m.phone, m.category, m.role, m.regdate) " +
            "from Member m " +
            "where m.email = :email")
    MemberDto getMemberInfo(@Param("email") String email);

//    @Query("select new com.party.dto.MemberDto(id, name, email, address, phone, category, role) from Member where category = :category")
//    List<MemberDto> findByCategoryEquals(@Param("category") String category);

    @Query("select new com.party.dto.MemberDto(m.id, m.name, m.email, m.address, m.phone, m.category, m.role, m.regdate) from Member m where m.category = :category and m.role = 'USER'")
    List<MemberDto> findByCategoryAndRole(@Param("category") String category);

    //카테고리가 'host'인 회원의 아이디를 찾아서 Role을 'HOST'로 업데이트 하는 쿼리문
    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.role = 'HOST' WHERE m.category = 'host' AND m.id = :id")
    void updateHostRole(@Param("id") Long id);





}