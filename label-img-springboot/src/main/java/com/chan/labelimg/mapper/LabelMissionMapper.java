package com.chan.labelimg.mapper;

import com.chan.labelimg.pojo.ImageLabel;
import com.chan.labelimg.pojo.ImageMission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/24/2021 4:12 PM
 */
@Mapper
public interface LabelMissionMapper {
    @Update("update image_mission set pointsX=#{pointsX},pointsY=#{pointsY}, remarks=#{remarks}, sizeX=#{sizeX},sizeY=#{sizeY} where imageID = #{imageID} and missionID= #{missionID}")
    public int updateImageMission(@Param("pointsX") String pointsX, @Param("pointsY") String PointsY, @Param("remarks") String remarks, @Param("sizeX") int sizeX, @Param("sizeY") int sizeY, @Param("missionID") int missionID, @Param("imageID") int imageID);

    @Select("select * from image_mission where missionID = #{id}")
    public List<ImageMission> getImageMissionByMissionID(@Param("id") int ID);
}
