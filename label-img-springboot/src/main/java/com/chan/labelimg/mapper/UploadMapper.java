package com.chan.labelimg.mapper;

import com.chan.labelimg.pojo.Img;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/5/2021 2:29 PM
 */
@Mapper
public interface UploadMapper {
    /**
     * @param fromID who upload the img
     * @param url    the url of the img
     * @return int code
     */
    @Insert("insert into img (url, fromID, kind) values (#{url}, #{fromID}, 0)")
    int uploadImg(@Param("fromID") int fromID, @Param("url") String url);

    /**
     * @param fromID fromID
     * @return all his images
     */
    @Select("select * from img where fromID = #{fromID} and kind = 0")
    List<Img> getImg(@Param("fromID") int fromID);

    /**
     * @param fromID fromID
     * @param url    url
     * @return int code
     */
    @Insert("insert into video (url, fromID, kind) values (#{url}, #{fromID}, 0)")
    int uploadVideo(@Param("fromID") int fromID, @Param("url") String url);
}
