package com.zhanghanlun.upyun.Mapper;


import com.zhanghanlun.upyun.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM upyun_user where username = #{username}")
    public User selectByUserName(@Param("username")String username);
}
