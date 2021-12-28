package com.chan.labelimg.mapper;

import com.chan.labelimg.pojo.Manager;
import com.chan.labelimg.pojo.User;
import org.apache.ibatis.annotations.Insert;
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
     * @param email email
     * @return User
     */
    @Select("select * from user where email = #{email}")
    User getByEmail(@Param("email") String email);

    /**
     * @param username username
     * @param password password
     * @return User
     */
    @Select("select * from user where username = #{username} and password = #{password}")
    User getByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("select * from manager where username = #{username} and password = #{password}")
    Manager getManagerByUsernamePassword(@Param("username") String username, @Param("password") String password);

    /**
     * @param username username
     * @param password password
     * @param email    email
     * @return int
     */
    @Insert("insert into user (username, password, email) values (#{username}, #{password}, #{email})")
    int signupUser(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    @Select("select username from user where id = #{ID}")
    String getNameByID(@Param("ID") int ID);
}
