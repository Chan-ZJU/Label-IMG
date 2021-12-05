package com.chan.labelimg.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: chen zheng
 * @Date: 12/5/2021 2:29 PM
 */
@Mapper
public interface ImgMapper {
    /**
     * @param fromID who upload the img
     * @param url    the url of the img
     * @return int code
     */
    @Insert("insert into img (url, fromID, kind) values (#{url}, #{fromID}, 0)")
    int uploadImg(@Param("fromID") int fromID, @Param("url") String url);
}
