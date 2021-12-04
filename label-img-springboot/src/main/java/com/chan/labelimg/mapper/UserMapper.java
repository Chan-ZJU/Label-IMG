package com.chan.labelimg.mapper;

import com.chan.labelimg.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author: chen zheng
 * @Date: 12/4/2021 3:38 PM
 */
@Mapper
public interface UserMapper {

    /**
     * @param username username
     * @return User
     */
    @Select("select * from user where username = #{username}")
    User getByUsername(@Param("username") String username);

    /**
     * @param username username
     * @param password password
     * @return User
     */
    @Select("select * from user where username = #{username} and password = #{password}")
    User getByUsernameAndPassword(@Param("username") String username,@Param("password") String password);
}
