package com.party.mapper;

import com.party.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapperInterface {

    @Select("select * from members where id = #{id}")
    Member SelectOne(@Param("id") Long id);

    @Select("select * from members")
    List<Member> SelectAll();

    @Update("UPDATE members SET name = #{member.name}, email = #{member.email}, address = #{member.address}, phone = #{member.phone} WHERE id = #{member.id}")
    int updateMember(@Param("member") Member member);

    @Delete("delete from members where id = #{id}")
    int Delete(@Param("id") Long id);




}
