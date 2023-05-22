package com.party.mapper;

import com.party.dto.HostProductDto;
import com.party.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapperInterface {

    @Select("SELECT r.*, u.name as user_name, u.email as user_email " +
            "FROM room r " +
            "JOIN user u ON r.user_id = u.id " +
            "WHERE r.id = #{id}")
    List<HostProductDto> getProductsByUser(@Param("id") String id);
}

