package com.chan.labelimg.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: chen zheng
 * @Date: 12/9/2021 5:15 PM
 */
@Mapper
public interface MissionMapper {
    @Insert("insert into mission(description, fromID, state) values (#{description}, #{fromID}, 0)")
    public int insertMission(@Param("description")String description, @Param("fromID") int fromID);
}
